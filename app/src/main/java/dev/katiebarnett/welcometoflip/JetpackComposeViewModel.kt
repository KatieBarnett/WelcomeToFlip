package dev.katiebarnett.welcometoflip

//@HiltViewModel
//class JetpackComposeViewModel @Inject constructor(
//) : StackViewModel() {
//
//
//    private val numberDeckTopPosition = MutableLiveData(1)
//    private val actionDeckTopPosition = MutableLiveData(0)
//
//    private val flipCardTopPosition = MutableLiveData(1)
//    private val flipCardBottomPosition = MutableLiveData(1)
//
//    val numberDeckTop = Transformations.map(numberDeckTopPosition) {
//        stack.getOrNull(it)?.number
//    }
//
//    val actionDeckTop = Transformations.map(actionDeckTopPosition) {
//        stack.getOrNull(it)?.action
//    }
//
//    val flipCardFront = Transformations.map(flipCardTopPosition) {
//        stack.getOrNull(it)?.number
//    }
//
//    val flipCardBack = Transformations.map(flipCardBottomPosition) {
//        stack.getOrNull(it)?.action
//    }
//
//    fun nextCardNumberDeck() {
//        numberDeckTopPosition.value?.let {
//            if (it < stack.size) {
//                numberDeckTopPosition.postValue(it + 1)
//            }
//        }
//    }
//
//    fun nextCardActionDeck() {
//        actionDeckTopPosition.value?.let {
//            if (it < stack.size) {
//                actionDeckTopPosition.postValue(it + 1)
//            }
//        }
//    }
//
//    fun previousCardNumberDeck() {
//        numberDeckTopPosition.value?.let {
//            if (it > 0) {
//                numberDeckTopPosition.postValue(it - 1)
//            }
//        }
//    }
//
//    fun previousCardActionDeck() {
//        actionDeckTopPosition.value?.let {
//            if (it > 0) {
//                actionDeckTopPosition.postValue(it - 1)
//            }
//        }
//    }
//
//    fun updateFlipCardPosition() {
//        flipCardTopPosition.postValue(numberDeckTopPosition.value)
//        flipCardBottomPosition.postValue(numberDeckTopPosition.value)
//    }
//}