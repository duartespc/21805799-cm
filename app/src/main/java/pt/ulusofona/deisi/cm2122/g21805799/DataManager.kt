package pt.ulusofona.deisi.cm2122.g21805799

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

object DataManager {

    var lastFireID: String = ""
        private set
    private val firesList = mutableListOf<Fire>()

    fun onStart() {
        var initialFire: Fire = Fire("1","20/04/2022","22:30","Praia das Maçãs","0","10","2","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","18/04/2022","19/04/2022","Muito vento. Foram 4h","Joao Maria","Informação não disponível")
        var initialFire2: Fire = Fire("2","21/04/2022","12:04","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire3: Fire = Fire("3","19/03/2022","12:04","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire4: Fire = Fire("4","25/03/2022","18:04","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire5: Fire = Fire("5","21/03/2022","12:34","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire6: Fire = Fire("6","29/04/2022","20:10","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire7: Fire = Fire("7","30/04/2022","14:04","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire8: Fire = Fire("8","01/05/2022","13:46","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")
        var initialFire9: Fire = Fire("9","03/05/2022","09:04","Banzão","1","5","1","Lisboa","Sintra","Colares","234.03","134.12","3","Activo","Praia das Maçãs","Detalhe Localização","ativo","19/04/2022","19/04/2022","","Joao Maria","Informação não disponível")

        firesList.add(initialFire)
        firesList.add(initialFire2)
        firesList.add(initialFire3)
        firesList.add(initialFire4)
        firesList.add(initialFire5)
        firesList.add(initialFire6)
        firesList.add(initialFire7)
        firesList.add(initialFire8)
        firesList.add(initialFire9)

    }

    fun getLastFire(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(5 * 1000)
            lastFireID = if (firesList.size > 0) firesList[firesList.size - 1].id else lastFireID
            onFinished(lastFireID)
        }
    }

    fun deleteFire(id: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(5 * 1000)
            val fire = firesList.find { it.id == id }
            firesList.remove(fire)
            onSuccess()
        }
    }

    fun getFiresList(onFinished: (List<Fire>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(5 * 1000)
            onFinished(firesList.toList())
        }
    }

    fun addToFiresList(fire: Fire) {
        Thread.sleep(5 * 1000)
        firesList.add(fire)
    }

    // Functions to populate DashBoard, will be retrieving from API later on...

    fun getActiveFires(): String {
        return "4"
    }

    fun getFiresToday(): String {
        return "13"
    }
    fun getFiresThisWeek(): String {
        return "27"
    }
    fun getFiresThisMonth(): String {
        return "68"
    }


}
