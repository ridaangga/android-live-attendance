package com.example.liveattendanceapp.views.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.liveattendanceapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AttendanceFragment : Fragment(), OnMapReadyCallback {

    private var mapAttendance: SupportMapFragment? = null

    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMaps()
    }

    private fun setupMaps() {
        //di dokumentasi google maps menggunakan fragment manager, sedangkan disini menggunakan childfragmentmanager. Karena ini halaman fragment(menggunakan fragment).
        //sedangkan yang di dokumentasi menggunakan activity.
        mapAttendance = childFragmentManager.findFragmentById(R.id.map_attendance) as SupportMapFragment
        mapAttendance?.getMapAsync(this) //this ini artinya get maps ketika OnMapReadyCallback yang ada di atas.
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //Coordinate Bisa di ganti sesuai tempat teman-teman masing-masing
        val sydney = LatLng(-6.918026, 106.931707)
        //val sydney = LatLng(-33.852, 151.211)
        map?.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        //Agar si maps pertama kali dibuka jadi ngezoom
        map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map?.animateCamera(CameraUpdateFactory.zoomTo(20f))
    }
}