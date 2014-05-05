package org.example.localizacion;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {
	private static final long TIEMPO_MIN=1000;// 10 segundos
private static final long DISTANCIA_MIN=5; //5 metros
private static final String[] A= {"n/d","preciso","impreciso"};
private static final String[] P={"n/d","bajo","medio","alto"};
private static final String[] E={"fuera de servicio","temporalmente no disponible","disponible"};
private LocationManager manejador;
private String proveedor;
private TextView salida;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

salida = (TextView) findViewById(R.id.salida);
manejador= (LocationManager) getSystemService(LOCATION_SERVICE);
log("Proveedores de localizacion: \n");
muestraProveedores();

Criteria criterio = new Criteria();
criterio.setCostAllowed(false);
criterio.setAltitudeRequired(false);
criterio.setAccuracy(Criteria.ACCURACY_FINE);
proveedor = manejador.getBestProvider(criterio, true);
log("Mejor proveedor: "+proveedor+"\n");
log("Comenzamos con la última localización conocida:");
Location localizacion = manejador.getLastKnownLocation(proveedor);
muestraLocaliz(localizacion);

}

@Override


protected void onResume() {
// TODO Auto-generated method stub
super.onResume();
//Activamos notificacione de localizacion
manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
}

@Override
protected void onPause() {
// TODO Auto-generated method stub
super.onPause();
manejador.removeUpdates(this);
}

//Metodos de la interfaz LocationListener
@Override
public void onLocationChanged(Location location) {
// TODO Auto-generated method stub
log("Nueva localización: ");
muestraLocaliz(location);
}

@Override
public void onProviderDisabled(String proveedor) {
// TODO Auto-generated method stub
log("Proveedor deshabilitado: "+proveedor+"\n");	
}

@Override
public void onProviderEnabled(String proveedor) {
// TODO Auto-generated method stub
log("Proveedor habilitado: "+proveedor+"\n");
}

@Override
public void onStatusChanged(String proveedor, int estado, Bundle extras) {
// TODO Auto-generated method stub
log("Cambia estado proveedor: "+proveedor+"estado= "+E[Math.max(0, estado)]+", extras="+extras+"\n");

}

//Metodos para mostrar información

private void log(String cadena){
salida.append(cadena+"\n");
}

private void muestraLocaliz(Location localizacion){
if(localizacion==null)
log("Localización desconocida\n");
else
log(localizacion.toString()+"\n");
}

private void muestraProveedores(){
log("Proveedor de localización: \n");
List<String> proveedores =manejador.getAllProviders();
for(String proveedor: proveedores){
muestraProveedor(proveedor);
}
}

private void muestraProveedor(String proveedor){
LocationProvider info=manejador.getProvider(proveedor);
log("LocationProvider["+"getName="+info.getName()
+",isProviderEnable="
+manejador.isProviderEnabled(proveedor)+",getAccuracy="
+A[Math.max(0, info.getAccuracy())]+",getPowerRequirement="
+P[Math.max(0, info.getPowerRequirement())]
+",hasMonetaryCost="+info.hasMonetaryCost()
+",requiresCell="+info.requiresCell()
+",requiresNetwork="+info.requiresNetwork()
+",requiresSatellite="+info.requiresSatellite()
+",supportAltitude="+info.supportsAltitude()
+",supportBearing="+info.supportsBearing()
+",supportBearing="+info.supportsBearing()
+"supportSpeed"+info.supportsSpeed()+" ]\n");
}

}

