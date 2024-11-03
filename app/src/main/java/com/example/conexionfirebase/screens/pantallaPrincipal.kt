package com.example.conexionfirebase.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.conexionfirebase.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.shadow
import kotlinx.coroutines.launch


val colorPrimario = Color(0xFF30C67C)
val colorSegundario =Color(0xFF82F4B1)
val colorGris =Color(0xFF3A3737)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavHostController) {
    val image = painterResource(R.drawable.imgperfil)
    var mostrarMenu by remember { mutableStateOf(false) }

    // Añadimos variables para el menu lateral principal
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    // Creamos una lista de opciones para el menu usando la clase NavigationItem
    val items = listOf(
        //Cada posicion de la lista contiente un texto con dos iconos (uno selecionado y otro sin)
        NavigationItem(
            title = "Inicio",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationItem(
            title = "Perfil",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
        ),
        NavigationItem(
            title = "Configuración",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
    )
    //para proporcionar la funcionalidad del drawer (menu) necesitamos envolver la pantalla
    //En un ModalNavigationDrawer
    ModalNavigationDrawer(
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ){
                // Dentro del menu definimos el drawerContent con cada componente con  items.forEachIndexed
                ModalDrawerSheet (
                    // Establecer el color de fondo negro
                    drawerContainerColor = Color.Black,
                    // Establecer un alto específico (ajusta el valor según tus necesidades)
                    modifier = Modifier
                        .width(300.dp)
                        //.width(IntrinsicSize.Min)//Ajusta el ancho al contenido
                        //.align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, colorPrimario), // Define el grosor y el color del borde
                            shape = RoundedCornerShape(8.dp) // Esquinas redondeadas con un radio de 8 dp
                        )
                ){
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        //Por cada posicion del item que hemos definido en items creamos un NavigationDrawerItem()
                        NavigationDrawerItem(
                            label = { Text(
                                text = item.title,
                                color = colorPrimario
                            ) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                                //Todo logica de navegacion
                            },
                            colors=NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = colorGris,  // Color cuando está seleccionado
                                unselectedContainerColor = Color.Black,  // Color cuando no está seleccionado
                                selectedTextColor = colorSegundario,  // Color del texto cuando está seleccionado
                                unselectedTextColor = colorPrimario,  // Color del texto cuando no está seleccionado
                                selectedIconColor = colorSegundario,  // Color del icono cuando está seleccionado
                                unselectedIconColor = colorPrimario  // Color del icono cuando no está seleccionado
                            ),
                            icon = {
                                Icon(
                                    //Dependiendo de si esta seleccionado o no tendra una apariencia u otra
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon

                                    } else item.unselectedIcon,
                                    contentDescription = item.title,
                                )
                                // Cambiar los colores de los items
                                NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = Color.DarkGray,
                                    unselectedContainerColor = Color.Black,
                                    selectedIconColor = Color.Green,
                                    unselectedIconColor = Color.Green,
                                    selectedTextColor = Color.Green,
                                    unselectedTextColor = Color.Green
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }

        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .drawBehind {
                            val borderSize = 1.dp.toPx()
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = borderSize
                            )
                        },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black,
                    ),
                    title = {
                        Text("Codifícate")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir el menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { mostrarMenu = !mostrarMenu }) {
                            Image(
                                painter = image,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.White, CircleShape)
                            )
                        }
                        DropdownMenu(
                            expanded = mostrarMenu,
                            onDismissRequest = { mostrarMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Configuración") },
                                onClick = { /*TODO*/ },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = "Configuración"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Perfil") },
                                onClick = { navController.navigate("pantallaPerfil") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Perfil"
                                    )
                                }
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            CuerpoDeLaPagina(innerPadding)
        }
    }
}
//Creamos una clase para cada opcion del menu lateral
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@Composable
fun CuerpoDeLaPagina(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF30C67C),  // Color superior
                        Color(0xFF82F4B1)   // Color inferior
                    )
                )
            )
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,


        ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.Black)
                .padding(horizontal = 8.dp)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            TarjetaCurso(
                image = painterResource(R.drawable.lineiconspython),
                descripcionCruso = stringResource(R.string.descripcion_curso_python),
                tipoCurso = "Curso",
                porcentaje = 23
            )
            TarjetaCurso(
                image = painterResource(R.drawable.cibkotlin),
                descripcionCruso = stringResource(R.string.descripcion_curso_kotlin),
                tipoCurso = "Curso",
                porcentaje = 56
            )
            TarjetaCurso(
                image = painterResource(R.drawable.uilreact),
                descripcionCruso = stringResource(R.string.descripcion_curso_react),
                tipoCurso = "Curso",
                porcentaje = 80
            )
            TarjetaCurso(
                image = painterResource(R.drawable.fileiconsfirebase),
                descripcionCruso = stringResource(R.string.descripcion_curso_fireBase),
                tipoCurso = "Taller",
                porcentaje = 90
            )
        }
    }
}

@Composable
fun TarjetaCurso(image: Painter, descripcionCruso: String, tipoCurso: String, porcentaje: Int) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xFF3A3737)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            CajaImagen(image)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = descripcionCruso,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            EstadisticasCurso(tipoCurso, porcentaje)
        }
    }
}

@Composable
fun CajaImagen(image: Painter) {
    //val image = painterResource(R.drawable.lineiconspython)
    Box(
        modifier = Modifier

            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = Color.Black
            )
            .padding(12.dp)
            .wrapContentHeight(),
        Alignment.CenterStart
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            alpha = 1F
        )

    }
}

@Composable
fun EstadisticasCurso(tipoCurso: String, porcentaje: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tipoCurso,
            modifier = Modifier
                .background(
                    color = Color.Black, // Color de fondo
                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
                )
                .padding(6.dp), // Espaciado interno para el texto
            color = Color.White // Color del texto
        )
        BarraPorcentaje(porcentaje)
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Abrir el menu",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}

@Composable
fun BarraPorcentaje(porcentaje: Int) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

    }
    Text(
        text = "${porcentaje.toString()}%",
        color = Color.White
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                color = Color.Black
            ),
        Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = porcentaje / 100f)
                .background(
                    color = Color(0xFF82F4B1)
                )
                .clip(RoundedCornerShape(12.dp))

        )
    }
}