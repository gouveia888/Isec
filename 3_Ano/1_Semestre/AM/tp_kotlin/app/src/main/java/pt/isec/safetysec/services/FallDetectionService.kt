package pt.isec.safetysec.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.ui.res.stringResource
import pt.isec.safetysec.R

class FallDetectionService(
    private val context: Context,
    private val onEmergencyDetected: (type: String) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Estados para Queda
    private val FALL_THRESHOLD = 2.5f
    private val FALL_IMPACT_MIN = 15.0f
    private val ACCIDENT_THRESHOLD = 38.0f
    private var lastFreeFallTimestamp: Long = 0

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // calculo da aceleração total
            val totalAcceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (totalAcceleration < FALL_THRESHOLD) {
                lastFreeFallTimestamp = System.currentTimeMillis()
            }

            val timeSinceFreeFall = System.currentTimeMillis() - lastFreeFallTimestamp

            //logica queda
            if (timeSinceFreeFall < 500 && totalAcceleration > FALL_IMPACT_MIN) {
                //se houve queda livre recente mesmo que o impacto seja muito forte
                onEmergencyDetected(context.getString(R.string.fall))
                lastFreeFallTimestamp = 0
                return
            }

            //logica acidente
            if (totalAcceleration > ACCIDENT_THRESHOLD) {
                onEmergencyDetected(context.getString(R.string.accident))
                return
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}