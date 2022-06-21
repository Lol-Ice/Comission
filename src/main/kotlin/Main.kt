val translationHistoryDay = 0 //история переводов за день
val translationHistoryMonth = 0 //история переводов за месяц
val transferAmount = 10_000 //сумма перевода
val card = "VK" //карты - VK, VISA, MAESTRO, MASTERCARD, MIR
val minComissionVISAMIR = 35 //минимальная комиссия для VISA и MIR
val commissionVISAMIR = 0.075 //комиссия VISA и MIR
val commissionMAESTROMASTERCARD = 0.06 //комиссия MAESTRO и MASTERCARD
val addCommissionMAESTROMASTERCARD = 20 //дополнительная комиссия по картам MAESTRO и MASTERCARD
val comissionVK = 0 //комиссия по карте VK
val actionComissionMAESTROMASTERCARD = 0 //комиссия по карте MAESTRO и MASTERCARD по акции

fun main() {

    println(calcCommission(actionLimits(), totalLimits()))

}

fun calcCommission(actionLimits: Boolean, totalLimits: Boolean): String {

    val transferAmountCommissionVK = transferAmount - comissionVK //вычет комиссии по карте VK
    val actionTransferAmountCommissionMAESTROMASTERCARD =
        transferAmount - actionComissionMAESTROMASTERCARD //вычет комиссии по карте MAESTRO и MASTERCARD по акции
    val totalComissionMAESTROMASTERCARD =
        transferAmount * commissionMAESTROMASTERCARD + addCommissionMAESTROMASTERCARD //комиссия по карте MAESTRO и MASTERCARD без акции
    val transferAmountCommissionMAESTROMASTERCARD =
        transferAmount - totalComissionMAESTROMASTERCARD //вычет комиссии по картам MAESTRO и MASTERCARD без акции
    val transferAmountMinCommissionVISAMIR =
        transferAmount - minComissionVISAMIR //вычет минимальной комиссии по картам VISA и MIR
    val totalCommissionVISAMIR = transferAmount * commissionVISAMIR //стандартная комиссия по картам VISA и MIR
    val transferAmountCommissionVISAMIR =
        transferAmount - totalCommissionVISAMIR //вычет стандартной комиссии по картам VISA и MIR

    return when {
        totalLimits == true && card == "VK" ->
            "Сумма перевода: $transferAmount руб\nПереведено: $transferAmountCommissionVK руб \nКомиссия за перевод: $comissionVK руб" //перевод по карте VK подпадающий под лимит

        totalLimits == true && actionLimits == true && card == "MAESTRO" || actionLimits == true && card == "MASTERCARD" ->
            "Сумма перевода: $transferAmount руб\nПереведено: $actionTransferAmountCommissionMAESTROMASTERCARD руб \nКомиссия за перевод: $actionComissionMAESTROMASTERCARD руб" //акция на переводы по картам MAESTRO и MASTERCARD

        totalLimits == true && actionLimits == false && card == "MAESTRO" || actionLimits == false && card == "MASTERCARD" ->
            "Сумма перевода: $transferAmount руб\nПереведено: $transferAmountCommissionMAESTROMASTERCARD руб \nКомиссия за перевод: $totalComissionMAESTROMASTERCARD руб" //переводы по картам MAESTRO и MASTERCARD не подпадающие под акции

        totalLimits == true && card == "VISA" && transferAmount * commissionVISAMIR < minComissionVISAMIR || card == "MIR" && transferAmount * commissionVISAMIR < minComissionVISAMIR ->
            "Сумма перевода: $transferAmount руб\nПереведено: $transferAmountMinCommissionVISAMIR руб \nКомиссия за перевод: $minComissionVISAMIR руб" //переводы по картам VISA и MIR с минимальной комиссией

        totalLimits == true && card == "VISA" && transferAmount * commissionVISAMIR >= minComissionVISAMIR || card == "MIR" && transferAmount * commissionVISAMIR >= minComissionVISAMIR ->
            "Сумма перевода: $transferAmount руб\nПереведено: $transferAmountCommissionVISAMIR руб \nКомиссия за перевод: $totalCommissionVISAMIR руб" //переводы по картам VISA и MIR со стандартной комиссией

        totalLimits == false ->
            "Отмена перевода. Превышены лимиты."
        else ->
            "Ошибка перевода"
    }
}

fun actionLimits(): Boolean { //лимиты на акции

    val actionMinTranslationHistoryMonth =
        300 //минимальная сумма перевода за месяц по акции по картам MAESTRO и MASTERCARD
    val actionMaxTranslationHistoryMonth =
        75_000 //максимальная сумма перевода за месяц по акции по картам MAESTRO и MASTERCARD

    return when {

        card == "MAESTRO" && translationHistoryMonth >= actionMinTranslationHistoryMonth && translationHistoryMonth <= actionMaxTranslationHistoryMonth -> true
        card == "MASTERCARD" && translationHistoryMonth >= actionMinTranslationHistoryMonth && translationHistoryMonth <= actionMaxTranslationHistoryMonth -> true
        else -> false
    }
}

fun totalLimits(): Boolean { //лимит на переводы

    val maxTranslationHistoryMonthVK = 40_000 //максимальная сумма перевода за месяц по карте VK
    val maxTranslationVK = 15_000 //максимальная сумма перевода по карте VK
    val translationDay = 150_000 //максимальная сумма перевода за день по картам
    val translationMonth = 600_000 //максимальная сумма перевода за месяц по картам

    return when {
        translationHistoryDay <= translationDay && translationHistoryMonth <= translationMonth && transferAmount <= translationDay -> true
        card == "VK" && translationHistoryMonth <= maxTranslationHistoryMonthVK && transferAmount <= maxTranslationVK -> true
        else -> false
    }
}
