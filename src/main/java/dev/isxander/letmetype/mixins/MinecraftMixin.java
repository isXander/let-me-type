package dev.isxander.letmetype.mixins;

import dev.isxander.letmetype.Storage;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyArg(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openChatScreen(Ljava/lang/String;)V", ordinal = 0))
    private String modifyInitialInputForChat(String input) {
        if (Storage.lastMessage != null)
            return Storage.lastMessage;
        return input;
    }

    @ModifyArg(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openChatScreen(Ljava/lang/String;)V", ordinal = 1))
    private String modifyInitialInputForCommand(String input) {
        if (Storage.lastMessage != null) {
            if (Storage.lastMessage.startsWith("/")) {
                return Storage.lastMessage;
            } else {
                Storage.lastMessage = null;
            }
        }
        return input;
    }
}
