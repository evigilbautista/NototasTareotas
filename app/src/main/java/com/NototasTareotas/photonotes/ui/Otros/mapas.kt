package com.NototasTareotas.photonotes.ui.Otros

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.Polyline
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.CopyrightOverlay

@Composable
fun OSMComposeMapa(
    modifier: Modifier = Modifier.fillMaxSize(),

    ) {



    // define properties with remember with default value
    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }

    // define marker state
    val depokMarkerState = rememberMarkerState(
        geoPoint = GeoPoint(20.1389, -101.15088),
        rotation = 90f // default is 0f
    )


    // setup mapProperties in side effect
    SideEffect {
        mapProperties = mapProperties
            .copy(isTilesScaledToDpi = true)
            .copy(tileSources = TileSourceFactory.MAPNIK)
            .copy(isEnableRotationGesture = true)
            .copy(zoomButtonVisibility = ZoomButtonVisibility.SHOW_AND_FADEOUT)
    }


    // define camera state
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(20.1389, -101.15088)
        zoom = 18.0 // optional, default is 5.0
    }


//    viewModel.directions_get("driving-car",
//        GeoPoint(20.139261336104898, -101.15026781862757),
//        GeoPoint(20.142110828753893, -101.1787275290486),
//        )

//    Log.d("GIVO",rutaUiState.value.toString())
    // define polyline
    var geoPointPoluLyne = remember {
        listOf(GeoPoint(20.1389,-101.15088),
            GeoPoint(20.1434,-101.1498),
            GeoPoint(20.14387,-101.15099),
            GeoPoint(20.14395,-101.15128),
            GeoPoint(20.14411,-101.15186))
        //rutaUiState.value.resp?.features[0].geometry.coordinates.map { GeoPoint(it[0],it[1]) }


    }



    val overlayManagerState = rememberOverlayManagerState()

    val ctx = LocalContext.current
    //Agregar nodo Mapa

    OpenStreetMap(cameraState = cameraState  ,
        properties = mapProperties,
        overlayManagerState = overlayManagerState,
        onFirstLoadListener = {
            val copyright = CopyrightOverlay(ctx)
            overlayManagerState.overlayManager.add(copyright) // add another overlay in this listener
        },
        modifier = modifier
    )
    {
        Marker(state = depokMarkerState, title="ITSUR", snippet = "Escuela") {
            // create info window node
            Column(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // setup content of info window
                Text(text = it.title)
                Text(text = it.snippet, fontSize = 10.sp)
            }

        }
        //if(rutaUiState.value.resp!=null){
            // geoPointPoluLyne = rutaUiState.value.resp!!.features[0].geometry.coordinates.map { GeoPoint(it[1],it[0]) }
            Polyline(geoPoints = geoPointPoluLyne) {

            }
        //}
    }
}