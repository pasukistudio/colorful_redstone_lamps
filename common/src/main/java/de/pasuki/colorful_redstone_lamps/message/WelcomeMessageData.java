package de.pasuki.colorful_redstone_lamps.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class WelcomeMessageData extends SavedData {
    private static final Identifier DATA_ID = Identifier.fromNamespaceAndPath(
            "colorful_redstone_lamps", "colorful_redstone_lamps_welcome"
    );

    // Feld MUSS vor dem CODEC stehen, sonst "cannot resolve symbol shownPlayers"
    private final Set<UUID> shownPlayers = new HashSet<>();

    // UUID Codec (stabil über String)
    private static final Codec<UUID> UUID_CODEC =
            Codec.STRING.xmap(UUID::fromString, UUID::toString);

    // Wir speichern die UUIDs als Liste
    private static final Codec<WelcomeMessageData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUID_CODEC.listOf()
                    .fieldOf("shown_players")
                    .forGetter(d -> List.copyOf(d.shownPlayers))
    ).apply(inst, WelcomeMessageData::fromList));

    public static final SavedDataType<WelcomeMessageData> TYPE = new SavedDataType<>(
            DATA_ID,
            WelcomeMessageData::new,
            CODEC,
            null
    );

    public WelcomeMessageData() {}

    private static WelcomeMessageData fromList(List<UUID> list) {
        WelcomeMessageData data = new WelcomeMessageData();
        data.shownPlayers.addAll(list);
        return data;
    }

    public static WelcomeMessageData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean wasShown(UUID playerId) {
        return shownPlayers.contains(playerId);
    }

    public void setShown(UUID playerId) {
        if (shownPlayers.add(playerId)) {
            setDirty();
        }
    }
}
