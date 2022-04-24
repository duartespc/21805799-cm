package pt.ulusofona.deisi.cm2122.g21805799.model

abstract class DataManager() {

    abstract fun getAllFires(onFinished: (List<Fire>) -> Unit)
    abstract fun insertAllFires(fires: List<Fire>, onFinished: () -> Unit)
    abstract fun clearAllFires(onFinished: () -> Unit)
    abstract fun insertFire(fire: Fire, onFinished: () -> Unit)
    abstract fun getRisk(municipality: String, onFinished: (String) -> Unit)
    abstract fun getLast7DaysTotal(onFinished: (String) -> Unit)
    abstract fun getActiveFiresTotal(onFinished: (String) -> Unit)


    /*abstract fun getLastFire(onFinished: (Fire) -> Unit)
    abstract fun deleteFire(fire: Fire)
    // Suspend indicates that the method will be executed on a side thread - using CoroutineScopeIO=
    // Functions to populate DashBoard, will be retrieving from API later on...
    abstract fun getActiveFires(onFinished: (List<Fire>) -> Unit)
    abstract fun getFiresToday(onFinished: (List<Fire>) -> Unit)
    abstract fun getFiresThisWeek(onFinished: (List<Fire>) -> Unit)
    abstract fun getFiresThisMonth(onFinished: (List<Fire>) -> Unit)*/


}
