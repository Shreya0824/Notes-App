import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:todo_app/Data_Model/NotesProvider.dart';
import 'package:todo_app/screens/Home_Screen.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context)=>NotesProvider(),
      child: MaterialApp(
        title: 'Flutter Demo',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: Home_Screen(),
      ),
    );
  }
}

