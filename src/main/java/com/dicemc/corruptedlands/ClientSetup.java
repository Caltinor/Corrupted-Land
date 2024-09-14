package com.dicemc.corruptedlands;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid=CorruptedLandMod.MOD_ID, bus=EventBusSubscriber.Bus.GAME, value= Dist.CLIENT)
public class ClientSetup {
	@SubscribeEvent
	public static void renderCorruption(RenderLevelStageEvent event) {
//		if (!event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS))
//			return;
//		PoseStack stack = event.getPoseStack();
//		Minecraft mc = Minecraft.getInstance();
//		Vec3 cameraPos = mc.getEntityRenderDispatcher().camera.getPosition();
//		stack.pushPose();
//		stack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
//		MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
//		VertexConsumer builder = buffer.getBuffer(RenderType.lines());
//		//TODO render the purple outlines
//
//		stack.popPose();
//		RenderSystem.disableDepthTest();
//		buffer.endBatch();
	}
}
