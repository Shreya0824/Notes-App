import 'package:flutter/cupertino.dart';

import 'notes.dart';

class NotesProvider extends ChangeNotifier{

  List<Notes> _notes = <Notes>[];
  List<Notes> get getNotes{
    return _notes;
  }
NotesProvider(){
    addNotes("title", "description");
}
  void addNotes(String title,String description){
    Notes note= new Notes(title, description);
    _notes.add(note);
    notifyListeners();
}
void removeData(int index){
    _notes.removeAt(index);
    notifyListeners();
}
}