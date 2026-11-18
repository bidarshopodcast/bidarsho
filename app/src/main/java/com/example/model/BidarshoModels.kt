package com.example.model

enum class MembershipTier(
    val titleFa: String,
    val badgeFa: String,
    val descriptionFa: String,
    val monthlyLabelFa: String
) {
    PUBLIC(
        titleFa = "بخش عمومی",
        badgeFa = "عضو عمومی",
        descriptionFa = "دسترسی رایگان به اپیزودهای پایه، مقالات منتخب و تمرین تنفس ۴-۷-۸",
        monthlyLabelFa = "رایگان"
    ),
    VIP(
        titleFa = "بخش اعضای ویژه (VIP)",
        badgeFa = "عضو ویژه VIP",
        descriptionFa = "دسترسی کامل به اپیزودهای تحلیلی ویژه، مدیتیشن‌ها و جلسات تلپاتی عمیق",
        monthlyLabelFa = "اشتراک ویژه"
    ),
    CLASS_MEMBER(
        titleFa = "بخش اعضای کلاس",
        badgeFa = "عضو کلاس‌ها",
        descriptionFa = "دسترسی کامل به تمام محتوای VIP، ثبت‌نام در کارگاه‌ها و اسلایدهای آموزشی پس از کلاس",
        monthlyLabelFa = "اشتراک کامل کلاس"
    );

    fun hasAccessTo(required: MembershipTier): Boolean {
        return when (this) {
            CLASS_MEMBER -> true
            VIP -> required == PUBLIC || required == VIP
            PUBLIC -> required == PUBLIC
        }
    }
}

data class ClassSlide(
    val slideNumber: Int,
    val title: String,
    val keyPoint: String,
    val explanation: String,
    val reflectionPrompt: String
)

data class OnlineClassItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val instructor: String,
    val sessionsCount: Int,
    val durationText: String,
    val scheduledDateFa: String,
    val unlockEpochMillis: Long,
    val summary: String,
    val syllabus: List<String>,
    val slides: List<ClassSlide>
) {
    fun isUnlocked(nowMillis: Long = System.currentTimeMillis()): Boolean {
        return nowMillis >= unlockEpochMillis
    }
}

data class EpisodeItem(
    val id: String,
    val title: String,
    val durationLabel: String,
    val category: String,
    val summary: String,
    val fullNotes: String,
    val actionStep: String,
    val requiredTier: MembershipTier
)

data class MeditationSession(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val category: String,
    val guidanceSteps: List<String>,
    val requiredTier: MembershipTier
)

data class LearnArticle(
    val id: String,
    val title: String,
    val readMinutes: Int,
    val topicTag: String,
    val summary: String,
    val paragraphs: List<String>,
    val requiredTier: MembershipTier
)

enum class BreathingPhase(
    val titleFa: String,
    val instructionFa: String,
    val totalSeconds: Int
) {
    READY(
        titleFa = "آماده شروع",
        instructionFa = "برای آغاز تمرین آرامبخش ۴-۷-۸ روی آدمک بزنید",
        totalSeconds = 0
    ),
    INHALE(
        titleFa = "دم عمیق",
        instructionFa = "آرام از طریق بینی به مدت ۴ ثانیه نفس بکشید",
        totalSeconds = 4
    ),
    HOLD(
        titleFa = "حبس نفس",
        instructionFa = "نفس را به آرامی به مدت ۷ ثانیه نگه دارید",
        totalSeconds = 7
    ),
    EXHALE(
        titleFa = "بازدم آرام",
        instructionFa = "به آرامی از طریق دهان و با ایجاد صدا به مدت ۸ ثانیه خارج کنید",
        totalSeconds = 8
    ),
    COMPLETED(
        titleFa = "پایان چرخه ۴ تایی",
        instructionFa = "آفرین! ۴ چرخه تنفس آگاهانه با موفقیت کامل شد. می‌توانید دوباره تکرار کنید.",
        totalSeconds = 0
    )
}

data class BreathingState(
    val isRunning: Boolean = false,
    val phase: BreathingPhase = BreathingPhase.READY,
    val secondsRemaining: Int = 0,
    val currentCycle: Int = 0,
    val totalCycles: Int = 4,
    val phaseProgress: Float = 0f
)
