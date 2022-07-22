package dev.katiebarnett.welcometoflip

//import dagger.hilt.android.AndroidEntryPoint
//import dev.katiebarnett.welcometoflip.databinding.JetpackComposeFlipFragmentBinding

//@AndroidEntryPoint
//class JetpackComposeTranslateFragment {//}: Fragment(R.layout.jetpack_compose_flip_fragment) {
//
//    private val viewModel: JetpackComposeViewModel by viewModels()
//
//    private lateinit var binding: JetpackComposeFlipFragmentBinding
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = JetpackComposeFlipFragmentBinding.inflate(inflater, container, false)
//        binding.lifecycleOwner = this
//        return binding.root
//    }
//
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.composeView.setContent {
//            MdcTheme {
//                FragmentScreen()
//            }
//        }
//    }
//
//    @Composable
//    private fun FragmentScreen() {
//
//        val numberCard = viewModel.numberDeckTop.observeAsState()
//        val actionCard = viewModel.actionDeckTop.observeAsState()
//        val flipCardFront = viewModel.flipCardFront.observeAsState()
//        val flipCardBack = viewModel.flipCardBack.observeAsState()
//        Column {
//            Decks(numberCard.value, actionCard.value, flipCardFront.value, flipCardBack.value)
//        }
//    }
//
//    @Composable
//    private fun Decks(@DrawableRes numberCard: Int?, @DrawableRes actionCard: Int?,
//                      @DrawableRes flipCardFront: Int?, @DrawableRes flipCardBack: Int?,
//                      modifier: Modifier = Modifier
//    ) {
//
//        val scope = rememberCoroutineScope()
//
//        var transitionEnabled by remember { mutableStateOf(false) }
//
//        var scale by remember { mutableStateOf(1f) }
//        var firstCardAlpha by remember { mutableStateOf(1f) }
//        var secondCardAlpha by remember { mutableStateOf(1f) }
//
//        var flipCardOffset by remember { mutableStateOf(0f) }
//
//        var cardSize by remember { mutableStateOf(IntSize.Zero) }
//        var numberDeckPosition by remember { mutableStateOf(Offset.Zero) }
//        var actionDeckPosition by remember { mutableStateOf(Offset.Zero) }
//
//        var flipCardVisibility by remember { mutableStateOf(false) }
//
//        Box {
//            Row(
//                modifier = modifier.padding(dimensionResource(id = R.dimen.spacing)).fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                numberCard?.let {
//                    Image(
//                        painter = painterResource(numberCard),
//                        contentDescription = "Number Deck",
//                        modifier = modifier.clickable(
//                            enabled = true,
//                            onClick = {
//                                transitionEnabled = !transitionEnabled
//                            }
//                        ).onGloballyPositioned { coordinates ->
//                            cardSize = coordinates.size
//                            numberDeckPosition = coordinates.positionInParent()
//                        }
//                    )
//                }
//                actionCard?.let {
//                    Image(
//                        painter = painterResource(actionCard),
//                        contentDescription = "Action Deck",
//                        modifier = modifier
//                            .onGloballyPositioned { coordinates ->
//                                actionDeckPosition = coordinates.positionInParent()
//                            }
//                    )
//                }
//            }
//            if (flipCardVisibility) {
//                Box(
//                    modifier.offset(flipCardOffset.dp)
//                        .width(cardSize.width.dp)
//                        .height(cardSize.height.dp)
//                ) {
//                    flipCardBack?.let {
//                        Image(
//                            painter = painterResource(flipCardBack),
//                            contentDescription = "Flip card back",
//                            modifier = modifier
//                        )
//                    }
//                    flipCardFront?.let {
//                        Image(
//                            painter = painterResource(flipCardFront),
//                            contentDescription = "Flip card front",
//                            modifier = modifier
//                        )
//                    }
//                }
//            }
//        }
//
//        if (transitionEnabled) {
//// Specify the key that should trigger the animation (e.g: when one part of your state changes)
//// If you keep Unit, the animation will run at the first time composition
//            LaunchedEffect(key1 = transitionEnabled) {
//                scope.launch {
//                    flipCardVisibility = true
//                    val translationAnimationSpec = tween<Float>(
//                        durationMillis = 5000,
//                        easing = FastOutLinearInEasing,
//                    )
//
//                    Log.d("HERE", "${numberDeckPosition.x} ${actionDeckPosition.x}")
//                    animate(initialValue = numberDeckPosition.x,
//                        targetValue = actionDeckPosition.x,
//                        animationSpec = translationAnimationSpec) { value: Float, _: Float ->
//                        flipCardOffset = value
//                    }
////                coroutineScope {
////                    val flipAnimationSpec = tween<Float>(
////                        durationMillis = 3000,
////                        easing = FastOutSlowInEasing,
////                    )
////                    launch {
////                        animate(initialValue = -180f,
////                            targetValue = 0f,
////                            animationSpec = flipAnimationSpec) { value: Float, _: Float ->
////                            flipCardOffset = value
////                        }
////
//
//
////                        repeat(5) {
////                            // Decrease animation duration per cycle to accelerate.
////                            // Not meant to be scalable as would need to be adjusted if cycle count would change
////                            val translationDuration = when (it) {
////                                0 -> 200
////                                1 -> 200
////                                2 -> 100
////                                3 -> 50
////                                4 -> 20
////                                else -> 0
////                            }
////
////                            val translationAnimationSpec = tween<Float>(
////                                durationMillis = translationDuration,
////                                easing = FastOutLinearInEasing,
////                            )
////
////                            animate(
////                                initialValue = 0f,
////                                targetValue = 1f,
////                                animationSpec = translationAnimationSpec
////                            ) { value: Float, _: Float ->
////                                offset = value
////                            }
////                            animate(
////                                initialValue = 1f,
////                                targetValue = 0f,
////                                animationSpec = translationAnimationSpec
////                            ) { value: Float, _: Float ->
////                                offset = value
////                            }
////                        }
////                    }
////                    launch {
////                        animate(
////                            initialValue = 0.9f,
////                            targetValue = 0.8f,
////                            animationSpec = transformationAnimationSpec
////                        ) { value: Float, _: Float ->
////                            scale = value
////                        }
////                    }
////                    launch {
////                        animate(
////                            initialValue = 1f,
////                            targetValue = 0.4f,
////                            animationSpec = transformationAnimationSpec
////                        ) { value: Float, _: Float ->
////                            firstCardAlpha = value
////                            secondCardAlpha = value
////                        }
////                    }
////                }
////
////                firstCardAlpha = 0f
////                secondCardAlpha =
////                    1f // Assuming we want to hide second card depending of what the shuffle result is
////                offset = 0.3f
////
////                delay(300)
////
////                coroutineScope {
////                    launch {
////                        animate(initialValue = 0.5f, targetValue = 1f) { value: Float, _: Float ->
////                            offset = value
////                        }
////                    }
////                    launch {
////                        animate(initialValue = 0.8f, targetValue = 1f) { value: Float, _: Float ->
////                            scale = value
////                        }
//                }
//            }
//        }
//    }
//}

