package net.litetex.sspb.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.dedicated.ServerPropertiesLoader;


/**
 * Backports the pause-when-empty-seconds functionality
 */
@Mixin(net.minecraft.server.dedicated.MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerPWEMixin extends MinecraftServerPWEMixin
{
	@Override
	public int getPauseWhenEmptySeconds()
	{
		return this.propertiesLoader.getPropertiesHandler().getInt("pause-when-empty-seconds", 60);
	}
	
	@Shadow
	@Final
	private ServerPropertiesLoader propertiesLoader;
}
