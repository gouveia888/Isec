import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:location/location.dart';
import 'package:path_provider/path_provider.dart';

import 'Data/CatFact.dart'; //flutter pub get (on console)

late Directory dir;
late File file;

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  dir = await getApplicationDocumentsDirectory();
  file = File("${dir.path}/userImage1");
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'amov - aula flutter 01',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a purple toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: .fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
      initialRoute: "Home",
      routes: {"/Home": (context) => const MyHomePage(title: 'AMOV - Aula Flutter 01'),
              "/SegundoEcra": (context) => const SegundoEcra(valorContador: 0,),
              "/TerceiroEcra": (context) => const TerceiroEcra(),
        }
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  static const String _catFactsUrl = 'https://catfact.ninja/facts';
  List<CatFact>? _catFacts;
  bool _fetchingData = false;

  Location location = new Location();
  bool _serviceEnabled=false;
  PermissionStatus _permissionGranted = PermissionStatus.denied;

  @override
  void initState(){
    super.initState();
    initLocation();

  }

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  void _resetCounter(){
  setState(() {
    _counter=0;
  });
  }

  void _decrementCounter(){
    setState(() {
      _counter--;
    });
  }

  Future<void> _fetchCatFacts() async {
    await Future.delayed(const Duration(seconds: 5));
    try {
      setState(() => _fetchingData = true);
      http.Response response = await http.get(Uri.parse(_catFactsUrl));
      if (response.statusCode == HttpStatus.ok) {
        debugPrint(response.body);
        final Map<String, dynamic> decodedData = json.decode(response.body);
        setState(() => _catFacts = (decodedData['data'] as List)
            .map((fact) => CatFact.fromJson(fact)).toList());
      }
    } catch (ex) {
      debugPrint('Something went wrong: $ex');
    } finally {
      setState(() => _fetchingData = false);
    }
  }

  Future<void> _fetchCatFactsClear() async {
    setState(() {
      _catFacts = null;
    });
  }


  LocationData _locationData = LocationData.fromMap({
    'latitude' : 0.0,
    'longitude' : 0.0
  });

  Future<void> initLocation() async {
    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await location.requestService();
      if (!_serviceEnabled) {
        return;
      }
    }
    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
      if (_permissionGranted != PermissionStatus.granted) {
        return;
      }
    }
    getLocation();
  }

  Future<void> getLocation() async {
    if (!_serviceEnabled ||
        _permissionGranted != PermissionStatus.granted) {
      return;
    }
    _locationData = await location.getLocation();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // TRY THIS: Try changing the color here to a specific color (to
        // Colors.amber, perhaps?) and trigger a hot reload to see the AppBar
        // change color while the other colors stay the same.
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          //
          // TRY THIS: Invoke "debug painting" (choose the "Toggle Debug Paint"
          // action in the IDE, or press "p" in the console), to see the
          // wireframe for each widget.
          mainAxisAlignment: .center,
          children: [

            const Text('You have pushed the button this many times:'),

            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),

            FilledButton(onPressed: _resetCounter, child: const Text("Reset")),
            if(_counter ==10)
              const FlutterLogo(size: 100)
            else
              const Text("Clique ate ao 10")
            ,
            FilledButton(onPressed: () async {
                final resultadoDoSegundoEcra =
                  await Navigator.push(context,
                  MaterialPageRoute(
                      builder:(_) => SegundoEcra(valorContador: _counter))
                  );

                if(resultadoDoSegundoEcra == null){
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("Não recebi valor de volta"),
                      backgroundColor: Colors.red,
                    ),
                  );
                }else{
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("Recebi o resultado: $resultadoDoSegundoEcra"),
                      backgroundColor: Colors.green,
                    ),
                  );
                }

                setState(() {
                  _counter = resultadoDoSegundoEcra;
                });

            }, child: const Text("Ir para segundo ecrã")),

            FilledButton(onPressed: (){
              Navigator.of(context).pushNamed("/TerceiroEcra");

            }, child: const Text ("Terceiro Ecra")),


            const SizedBox(height: 20),
            //Exemplo chamada assincrona
            FutureBuilder<String>(
              future: _fetchAnAsyncString(),
              builder: (
                  BuildContext context, AsyncSnapshot<String> snapshot
                  ) {
                if (snapshot.hasData) {
                  return Text(snapshot.data!);
                } else if (snapshot.hasError) {
                  return const Text('Oops, something happened');
                } else {
                  return const CircularProgressIndicator();
                }
              },
            ),

            //Exemplo chamada api
            /*FutureBuilder<http.Response>(
              future: http.get(Uri.parse(_catFactsUrl)),
              builder: (
              BuildContext context, AsyncSnapshot<http.Response> snapshot
              ) {
                  if (snapshot.hasData) {
                    return Expanded(
                      child: SingleChildScrollView( child: Text(snapshot.data!.body))
                    );
                  } else if (snapshot.hasError) {
                    return const Text('Oops, something happened');
                  } else {
                    return const CircularProgressIndicator();
                  }
              },*/
              //apresenta o json em formato de lista
                ElevatedButton(
                  onPressed: _fetchCatFacts,
                  child: const Text('Fetch cat facts'),
                ),
                if (_fetchingData) const CircularProgressIndicator(),
                if (!_fetchingData && _catFacts != null && _catFacts!.isNotEmpty)
                  Expanded(
                    child: ListView.separated(
                      itemCount: _catFacts!.length,
                      separatorBuilder: (_, __) => const Divider(thickness: 2.0),
                      itemBuilder: (BuildContext context, int index) =>
                          ListTile(
                            title: Text('Cat fact #$index'),
                            subtitle: Text(_catFacts![index].fact),
                          ),
                    ),
                  ),

            //limpar a lista
            ElevatedButton(
              onPressed: _fetchCatFactsClear,
              child: const Text('Clear cat facts'),
            ),
            
            Row(
              children: [
                Image.asset('images/1.png'),
                SizedBox(height:50, child: Image.network('https://wayf.ipc.pt/IPCds/images/logo_ipc2.png')),
              ],
            ),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Text('Lat: ${_locationData.latitude}'),
                Text('Lon: ${_locationData.longitude}')
              ],
            ),

            ElevatedButton(
                onPressed: getLocation,
                child: const Text('Get location')
            ),

            ElevatedButton(
                onPressed: () {
                  location.onLocationChanged.listen(
                          (LocationData currentLocation) {
                        setState(() {_locationData = currentLocation;});
                      }
                  );
                },
                child: const Text('Activate continuous location')
            ),

            StreamBuilder(
                stream: location.onLocationChanged,
                builder: (context, location) => Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Text('Lat: ${location.data?.latitude}'),
                    Text('Lon: ${location.data?.longitude}')
                  ],
                )
            ),

            ElevatedButton(
                onPressed: () async {
                  final imgFile = await ImagePicker().pickImage(source: ImageSource.gallery);
                  if (imgFile != null) {
                    await imgFile.saveTo(file.path);
                    FileImage(file).evict(); // avoid cache
                  }
                  setState(() {});
                },
                child: const Text("Pick Image")
            ),
            if (file.existsSync())
              Image.file(key: UniqueKey(), file),
            
            ],
        ),
      ),
      floatingActionButton:
      Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child:
        Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              FloatingActionButton(
              onPressed: _decrementCounter,
              tooltip: 'Decrement',
              child: const Icon(Icons.remove),
            ),
              FloatingActionButton(
                onPressed: _incrementCounter,
                tooltip: 'Increment',
                child: const Icon(Icons.add),
              ),
            ],
        )
      )
    );
  }
}

Future<String> _fetchAnAsyncString() async {
  await Future.delayed(const Duration(seconds: 5));
  return Future.value('Hello world, from an aysnc call!');
}

class SegundoEcra extends StatelessWidget {
  final int valorContador;

  const SegundoEcra({super.key, required this.valorContador});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Isto é o segundo ecrã"),),
      body: Center(
        child: Column(
          children: [
            Column(
              children: [
                Text("O valor do contador é: $valorContador"),
                FilledButton(onPressed: (){
                  int dobro = valorContador*2;
                  Navigator.of(context).pop(dobro);
                }, child: Text("Voltar atrás")),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class TerceiroEcra extends StatelessWidget {
  const TerceiroEcra({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Isto é o terceiro ecrã"),),
      body: const Center(
        child: Column(
          children: [
            const Text("Conteudo do terceiro ecra"),
            const FlutterLogo(size: 100)
          ],
        ),
      ),
    );
  }
}
