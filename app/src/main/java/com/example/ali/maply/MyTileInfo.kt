package com.example.ali.maply

import android.graphics.Bitmap
import android.util.Log
import com.mousebird.maply.RemoteTileInfo
import com.squareup.okhttp.Request

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.MalformedURLException

import java.net.URL
import java.util.*

/**
 * Created by Ali on 2017-11-26.
 */

class MyTileInfo(inBase: String?, inExt: String?, inMinZoom: Int, inMaxZoom: Int) : RemoteTileInfo(inBase, inExt, inMinZoom, inMaxZoom) {

    internal val OSGEO_WMS = "http://map.ir/" + "shiveh?" +
            "service=WMS" +
            "&version=1.1.0" +
            "&EXCEPTIONS=application/vnd.ogc.se_inimage" +
            "&request=GetMap" +
            "&layers=Shiveh:ShivehGSLD256" +
            "&width=256" +
            "&height=256" +
            "&srs=EPSG:3857" +
            "&format=image/png" +
            "&bbox=%f,%f,%f,%f"

    override
    fun buildURL(x: Int, y: Int, level: Int): URL? {
        var url: String? = null
        var urlt: String? = getTileUrl(x,y,level)
//        if (replaceURL)
//            url = baseURLs[x % baseURLs.size].replace("{x}", "" + x).replace("{y}", "" + y).replace("{z}", "" + level)
//        else
//            url = baseURLs[x % baseURLs.size] + level + "/" + x + "/" + y
//        if (url != null && ext != null)
//            url = url + "." + ext

        var retURL: URL? = null
        try {
            retURL = URL(urlt)
        } catch (e: Exception) {
        }

        return retURL
    }

    private fun getTileUrl(x: Int, y: Int, z: Int): String {
        val bbox = getBoundingBox(x, y, z)
        val s = String.format(Locale.US, OSGEO_WMS, bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY])
        Log.d("WMSDEMO", s)
        val url: URL
        try {
            url = URL(s)
        } catch (e: MalformedURLException) {
            throw AssertionError(e)
        }

        return url.toString()
    }

    protected fun getBoundingBox(x: Int, y: Int, zoom: Int): DoubleArray {

        val tileSize = MAP_SIZE / Math.pow(2.0, zoom.toDouble())
        val minx = TILE_ORIGIN[ORIG_X] + x * tileSize
        val maxx = TILE_ORIGIN[ORIG_X] + (x + 1) * tileSize
        val miny = TILE_ORIGIN[ORIG_Y] - (y + 1) * tileSize
        val maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize

        val bbox = DoubleArray(4)
        bbox[MINX] = minx
        bbox[MINY] = miny
        bbox[MAXX] = maxx
        bbox[MAXY] = maxy

        return bbox
    }

    companion object {

        private val TILE_ORIGIN = doubleArrayOf(-20037508.34789244, 20037508.34789244)
        // array indexes for that data
        private val ORIG_X = 0
        private val ORIG_Y = 1 // "

        // Size of square world map in meters, using WebMerc projection.
        private val MAP_SIZE = 20037508.34789244 * 2

        // array indexes for array to hold bounding boxes.
        protected val MINX = 0
        protected val MAXX = 1
        protected val MINY = 2
        protected val MAXY = 3
    }

}
