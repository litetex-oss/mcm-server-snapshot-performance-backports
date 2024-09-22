package net.litetex.sspb.mixin;

import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.util.Util;
import net.minecraft.util.thread.ThreadExecutor;


/**
 * Backport of fix for MC-183518
 */
@Mixin(net.minecraft.server.MinecraftServer.class)
public abstract class MinecraftServerFixParkNanosMixin
{
	@Unique
	private boolean waitingForNextTickNew;
	
	/**
	 * @author litetex
	 * @reason Backport of fix for MC-183518
	 */
	@Overwrite
	@SuppressWarnings("rawtypes")
	public void runTasksTillTickEnd()
	{
		((ThreadExecutor)(Object)this).runTask();
		this.waitingForNextTickNew = true;
		try
		{
			this.runTasks(() -> !this.shouldKeepTicking());
		}
		finally
		{
			this.waitingForNextTickNew = false;
		}
	}
	
	/**
	 * @author litetex
	 * @reason Backport of fix for MC-183518
	 */
	@SuppressWarnings("checkstyle:MagicNumber")
	@Overwrite
	public void waitForTasks()
	{
		final boolean bl = this.shouldPushTickTimeLog();
		final long l = bl ? Util.getMeasuringTimeNano() : 0L;
		final long m = this.waitingForNextTickNew ? this.tickStartTimeNanos - Util.getMeasuringTimeNano() : 100000L;
		LockSupport.parkNanos("waiting for tasks", m);
		if(bl)
		{
			this.waitTime += Util.getMeasuringTimeNano() - l;
		}
	}
	
	@Shadow
	private long tickStartTimeNanos;
	
	@Shadow
	public abstract void runTasks(final BooleanSupplier stopCondition);
	
	@Shadow
	protected abstract boolean shouldKeepTicking();
	
	@Shadow
	public abstract boolean shouldPushTickTimeLog();
	
	@Shadow
	private long waitTime;
}
