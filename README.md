#Training:  Memory (Ejecutar en Dispositivo)

Tenemos una aplicación muy simple. Queremos saber que tendencia tiene el usuario para swipear la pantalla.
En cada swipe, se cambiará de pantalla y por detrás la aplicación trackeará el nombre del color al cual fue. 
- Al **minimizar la pantalla**  la aplicación escribirá en el **Logcat** los pasos que hizo hasta ese momento
- Al hacer un **tap largo** sobre la pantalla la aplicación escribirá en el **Logcat** todos los pasos que hizo desde que abrió la app

**El proyecto actualmente está inestablesi se ejecuta en un celular, luego de ejecutar uno o dos pasos deja de funcionar. Podrías ayudnos  a arreglarlo para que funcione bien?**

Tips:
- Pueden ver el **Logcat** en Android Studio ingresando en el buscador la palabra *StepsTracker*. Sinó pueden hacer `adb logcat | grep "StepsTracker"`
- Necesitarán utilizar el *Android Device Monitor* para entender un poco qué está sucediendo.
- En caso de que no terminen de entender qué sucede con el Device Monitor, pueden hacer un dump del Heap y analizarlo con el *Memory Analizer Tool*

[Memory Analizer Tool - Download](http://www.eclipse.org/downloads/download.php?file=/mat/1.4/rcp/MemoryAnalyzer-1.4.0.20140604-macosx.cocoa.x86_64.zip)

*Ayuda con carpa*:
Estará manejando correctamente la cantidad de objetos la app?
