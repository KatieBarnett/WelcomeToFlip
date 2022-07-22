package dev.katiebarnett.welcometoflip

//import dev.katiebarnett.welcometoflip.databinding.JetpackComposeFlipFragmentBinding

//@AndroidEntryPoint
//class JetpackComposeFlipFragment {//}: Fragment(R.layout.jetpack_compose_flip_fragment) {
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
//        var flipCardOffset by remember { mutableStateOf(0f) }
//
//        var cardSize by remember { mutableStateOf(IntSize.Zero) }
//        var numberDeckPosition by remember { mutableStateOf(Offset.Zero) }
//        var actionDeckPosition by remember { mutableStateOf(Offset.Zero) }
//
//        var flipCardVisibility by remember { mutableStateOf(false) }
//
//        var frontCardAlpha by remember { mutableStateOf(1.0f) }
//        var frontCardRotationY by remember { mutableStateOf(0f) }
//
//        var backCardAlpha by remember { mutableStateOf(1.0f) }
//        var backCardRotationY by remember { mutableStateOf(-180f) }
//
//        Box {
//            Row(
//                modifier = modifier
//                    .padding(dimensionResource(id = R.dimen.spacing))
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                numberCard?.let {
//                    Image(
//                        painter = painterResource(numberCard),
//                        contentDescription = "Number Deck",
//                        modifier = modifier
//                            .clickable(
//                                enabled = true,
//                                onClick = {
//                                    transitionEnabled = !transitionEnabled
//                                }
//                            )
//                            .onGloballyPositioned { coordinates ->
//                                cardSize = coordinates.size
//                                numberDeckPosition = coordinates.positionInParent()
//                            }
//                    )
//                }
//                actionCard?.let {
//                    Image(
//                        painter = painterResource(actionCard),
//                        contentDescription = "Action Deck",
//                        modifier = modifier
//                        .onGloballyPositioned { coordinates ->
//                            actionDeckPosition = coordinates.positionInParent()
//                        }
//                    )
//                }
//            }
//            if (flipCardFront != null && flipCardBack != null) {
//                AnimatingBox(
//                    rotated = transitionEnabled, cardSize = cardSize,
//                    onRotateComplete = {
//                        Log.d("HERE", "Rotation complete: $it")
//                    },
//                    modifier = modifier.offset(flipCardOffset.dp)
//                        .width(cardSize.width.dp)
//                        .height(cardSize.height.dp),
//                    targetFrontCard = flipCardFront,
//                    targetBackCard = flipCardBack
//                )
//            }
//        }
//    }
//
//    @Preview(showBackground = true)
//    @Composable
//    fun DecksPreview() {
//        Decks(R.drawable.number_12, R.drawable.action_1, R.drawable.number_12, R.drawable.action_2)
//    }
//}