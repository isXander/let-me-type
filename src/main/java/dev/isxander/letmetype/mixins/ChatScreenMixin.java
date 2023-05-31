package dev.isxander.letmetype.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.letmetype.Storage;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow protected EditBox input;
    private boolean firstInit = true;

    @Inject(method = "init", at = @At("RETURN"))
    private void onInitWidgets(CallbackInfo ci) {
        if (firstInit) {
            firstInit = false;
            if (Storage.caretPos != -1) {
                input.setCursorPosition(Storage.caretPos);
                input.setHighlightPos(Storage.caretPos);
                Storage.caretPos = -1;
            }
        }
    }
    // Update last message when message is edited
    @Inject(method = "onEdited", at = @At("RETURN"))
    private void onMessageEdited(String message, CallbackInfo ci) {
        Storage.lastMessage = input.getValue();
        Storage.caretPos = input.getCursorPosition();
    }

    // Clear last message when message is sent, the user wanted to do this
    @Inject(method = "handleChatInput", at = @At("RETURN"))
    private void onMessageSent(String text, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            Storage.lastMessage = null;
            Storage.caretPos = -1;
        }
    }

    // Clear last message when chat is closed, the user wanted to do this
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onEscapePressed(int key, int scancode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (key == InputConstants.KEY_ESCAPE) {
            Storage.lastMessage = null;
            Storage.caretPos = -1;
        }
    }
}
