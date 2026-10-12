package com.example.practicalwork4_variant6.composables.lesson16

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork4_variant6.composables.common.MenuButton
import com.example.practicalwork4_variant6.composables.common.ScreenTitle
import com.example.practicalwork4_variant6.domain.BiogasStation
import com.example.practicalwork4_variant6.viewmodel.LocationViewModel
import com.example.practicalwork4_variant6.viewmodel.StationListViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val KYIV = LatLng(50.4501, 30.5234)

@Composable
private fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        onResult(results[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            results[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            onResult(true)
        } else {
            launcher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }
}

@Composable
fun Lesson16Basic(
    stationViewModel: StationListViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    val stations by stationViewModel.stations.collectAsState()
    val userLocation by locationViewModel.userLocation.collectAsState()
    val status by locationViewModel.statusMessage.collectAsState()

    RequestLocationPermission { granted ->
        if (granted) locationViewModel.refreshLocation() else locationViewModel.onPermissionDenied()
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(KYIV, 9.5f)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: маркери об'єктів + геопозиція")
        Text("Статус GPS: $status")
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(380.dp),
            cameraPositionState = cameraPositionState
        ) {
            stations.forEach { station ->
                Marker(
                    state = MarkerState(LatLng(station.latitude, station.longitude)),
                    title = station.name,
                    snippet = station.type.label
                )
            }
            userLocation?.let { loc ->
                Marker(
                    state = MarkerState(LatLng(loc.latitude, loc.longitude)),
                    title = "Ви тут",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
    }
}

@Composable
fun Lesson16Medium(
    stationViewModel: StationListViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    val stations by stationViewModel.stations.collectAsState()
    val selectedId by stationViewModel.selectedStationId.collectAsState()
    val userLocation by locationViewModel.userLocation.collectAsState()

    RequestLocationPermission { granted ->
        if (granted) locationViewModel.refreshLocation() else locationViewModel.onPermissionDenied()
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(KYIV, 9.5f)
    }

    val selectedStation = stations.find { it.id == selectedId }
    LaunchedEffect(selectedStation) {
        selectedStation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 13f
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: синхронізація списку і мапи")
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(260.dp),
            cameraPositionState = cameraPositionState
        ) {
            stations.forEach { station ->
                Marker(
                    state = MarkerState(LatLng(station.latitude, station.longitude)),
                    title = station.name,
                    snippet = "${station.type.label} — ${station.status.label}", // InfoWindow при натисканні
                    onClick = { stationViewModel.selectStation(station.id); false }
                )
            }
            userLocation?.let { loc ->
                Marker(
                    state = MarkerState(LatLng(loc.latitude, loc.longitude)),
                    title = "Ви тут",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
        LazyColumn {
            items(stations, key = { it.id }) { station ->
                StationListRow(station, isSelected = station.id == selectedId) {
                    stationViewModel.selectStation(station.id)
                }
            }
        }
    }
}

@Composable
fun Lesson16Advanced(
    stationViewModel: StationListViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    val processedStations by stationViewModel.processedStations.collectAsState()
    val selectedId by stationViewModel.selectedStationId.collectAsState()
    val userLocation by locationViewModel.userLocation.collectAsState()

    RequestLocationPermission { granted ->
        if (granted) locationViewModel.refreshLocation() else locationViewModel.onPermissionDenied()
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(KYIV, 9.5f)
    }
    val selectedStation = processedStations.find { it.id == selectedId }
    LaunchedEffect(selectedStation) {
        selectedStation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 13f
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: фільтрація + мапа + список")
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(220.dp),
            cameraPositionState = cameraPositionState
        ) {
            processedStations.forEach { station ->
                Marker(
                    state = MarkerState(LatLng(station.latitude, station.longitude)),
                    title = station.name,
                    snippet = "${station.powerKw} кВт, CH4 ${station.methaneLevel}%",
                    onClick = { stationViewModel.selectStation(station.id); false }
                )
            }
            userLocation?.let { loc ->
                Marker(
                    state = MarkerState(LatLng(loc.latitude, loc.longitude)),
                    title = "Ви тут",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
        MenuButton("Фільтр: лише «У роботі», сортувати за потужністю") {
            stationViewModel.updateFilterStatus(com.example.practicalwork4_variant6.domain.StationStatus.ONLINE)
            stationViewModel.updateSortBy(com.example.practicalwork4_variant6.domain.StationSortBy.POWER)
        }
        MenuButton("Скинути фільтри") {
            stationViewModel.updateFilterStatus(null)
            stationViewModel.updateFilterType(null)
        }
        LazyColumn {
            items(processedStations, key = { it.id }) { station ->
                StationListRow(station, isSelected = station.id == selectedId) {
                    stationViewModel.selectStation(station.id)
                }
            }
        }
    }
}

@Composable
private fun StationListRow(station: BiogasStation, isSelected: Boolean, onClick: () -> Unit) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        tonalElevation = if (isSelected) 6.dp else 0.dp,
        onClick = onClick
    ) {
        Text(
            "${if (isSelected) "▶ " else ""}${station.name} (${station.type.label})",
            modifier = Modifier.padding(10.dp)
        )
    }
}
