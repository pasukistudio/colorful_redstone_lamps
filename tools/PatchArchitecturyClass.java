import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Rewrites Architectury's NeoForge 26.1 event reference from BreakBlockEvent
 * (introduced after 26.1) to the 26.1 BlockEvent.BreakEvent API.
 */
public final class PatchArchitecturyClass {
    private static final String TARGET_CLASS = "dev/architectury/event/forge/EventHandlerImplCommon.class";
    private static final String OLD_NAME = "net/neoforged/neoforge/event/level/block/BreakBlockEvent";
    private static final String NEW_NAME = "net/neoforged/neoforge/event/level/BlockEvent$BreakEvent";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: PatchArchitecturyClass <input.jar> <output.jar>");
        }

        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);
        Files.createDirectories(output.getParent());

        try (ZipFile source = new ZipFile(input.toFile());
             ZipOutputStream destination = new ZipOutputStream(Files.newOutputStream(output))) {
            boolean patched = false;
            var entries = source.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                ZipEntry copy = new ZipEntry(entry.getName());
                copy.setTime(entry.getTime());
                destination.putNextEntry(copy);
                byte[] bytes = source.getInputStream(entry).readAllBytes();
                if (TARGET_CLASS.equals(entry.getName())) {
                    bytes = patchClass(bytes);
                    patched = true;
                }
                destination.write(bytes);
                destination.closeEntry();
            }
            if (!patched) {
                throw new IOException("Architectury event handler class was not found");
            }
        }
    }

    private static byte[] patchClass(byte[] input) throws IOException {
        try (var in = new java.io.DataInputStream(new ByteArrayInputStream(input));
             var output = new ByteArrayOutputStream();
             var out = new java.io.DataOutputStream(output)) {
            out.writeInt(in.readInt());
            out.writeShort(in.readUnsignedShort());
            out.writeShort(in.readUnsignedShort());
            int count = in.readUnsignedShort();
            out.writeShort(count);

            boolean replaced = false;
            for (int index = 1; index < count; index++) {
                int tag = in.readUnsignedByte();
                out.writeByte(tag);
                switch (tag) {
                    case 1 -> {
                        int length = in.readUnsignedShort();
                        byte[] value = in.readNBytes(length);
                        String text = new String(value, StandardCharsets.UTF_8);
                        if (text.contains(OLD_NAME)) {
                            value = text.replace(OLD_NAME, NEW_NAME).getBytes(StandardCharsets.UTF_8);
                            replaced = true;
                        }
                        out.writeShort(value.length);
                        out.write(value);
                    }
                    case 3, 4 -> out.writeInt(in.readInt());
                    case 5, 6 -> {
                        out.writeLong(in.readLong());
                        index++;
                    }
                    case 7, 8, 16, 19, 20 -> out.writeShort(in.readUnsignedShort());
                    case 9, 10, 11, 12, 17, 18 -> {
                        out.writeShort(in.readUnsignedShort());
                        out.writeShort(in.readUnsignedShort());
                    }
                    case 15 -> {
                        out.writeByte(in.readUnsignedByte());
                        out.writeShort(in.readUnsignedShort());
                    }
                    default -> throw new IOException("Unexpected class-file constant-pool tag: " + tag);
                }
            }
            out.write(in.readAllBytes());
            if (!replaced) {
                throw new IOException("BreakBlockEvent reference was not found");
            }
            return output.toByteArray();
        }
    }
}
