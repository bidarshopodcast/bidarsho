package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BidarshoCatalog
import com.example.model.BreathingPhase
import com.example.model.MembershipTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name matches Bidarsho Persian title`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("بیدارشو", appName)
    }

    @Test
    fun `membership tier hierarchy enforces access rules`() {
        assertTrue(MembershipTier.PUBLIC.hasAccessTo(MembershipTier.PUBLIC))
        assertFalse(MembershipTier.PUBLIC.hasAccessTo(MembershipTier.VIP))
        assertFalse(MembershipTier.PUBLIC.hasAccessTo(MembershipTier.CLASS_MEMBER))

        assertTrue(MembershipTier.VIP.hasAccessTo(MembershipTier.PUBLIC))
        assertTrue(MembershipTier.VIP.hasAccessTo(MembershipTier.VIP))
        assertFalse(MembershipTier.VIP.hasAccessTo(MembershipTier.CLASS_MEMBER))

        assertTrue(MembershipTier.CLASS_MEMBER.hasAccessTo(MembershipTier.PUBLIC))
        assertTrue(MembershipTier.CLASS_MEMBER.hasAccessTo(MembershipTier.VIP))
        assertTrue(MembershipTier.CLASS_MEMBER.hasAccessTo(MembershipTier.CLASS_MEMBER))
    }

    @Test
    fun `classes lock and unlock based on scheduled timestamp`() {
        val classes = BidarshoCatalog.onlineClasses
        assertEquals(4, classes.size)
        val nov2026Millis = 1795000000000L
        assertTrue(classes[0].isUnlocked(nov2026Millis))
        assertTrue(classes[1].isUnlocked(nov2026Millis))
        assertFalse(classes[2].isUnlocked(nov2026Millis))
        assertFalse(classes[3].isUnlocked(nov2026Millis))

        // After future unlock date, classes unlock automatically
        val jan2027Millis = 1800000000000L
        assertTrue(classes[2].isUnlocked(jan2027Millis))
        assertTrue(classes[3].isUnlocked(jan2027Millis))
    }

    @Test
    fun `breathing 4-7-8 phases have exact second durations`() {
        assertEquals(4, BreathingPhase.INHALE.totalSeconds)
        assertEquals(7, BreathingPhase.HOLD.totalSeconds)
        assertEquals(8, BreathingPhase.EXHALE.totalSeconds)
    }
}
