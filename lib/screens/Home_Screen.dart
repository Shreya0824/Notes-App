import 'package:flutter/material.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:provider/provider.dart';
import 'package:todo_app/Data_Model/NotesProvider.dart';
import 'package:todo_app/Data_Model/notes.dart';
import 'package:lottie/lottie.dart';

class Home_Screen extends StatelessWidget {
  const Home_Screen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.lightBlueAccent,
        appBar: AppBar(
        titleSpacing: 0.0,
        toolbarHeight: 350,
        title: Lottie.asset('assets/65683-notes-animation (1).json')
        //Image.network("https://9to5mac.com/wp-content/uploads/sites/6/2019/11/how-to-quickly-select-move-delete-notes-iphone-ipad-two-finger-tap.jpeg?quality=82&strip=all",fit: BoxFit.cover,)
    ),
    body: Consumer<NotesProvider>(
      builder:(context, NotesProvider data, child){
        return data.getNotes.length!=null ? ListView.builder(
            itemCount: data.getNotes.length,
            itemBuilder: (context,index)
          {
            return CardList(data.getNotes[index],index);
          }
        ): Center(child: Text("Add Notes"),);
      }
    ),
      floatingActionButton: FloatingActionButton(
        onPressed: (){
          showDialogBox(context);
        },
        backgroundColor: Colors.white,
        child: Icon(Icons.add,color: Colors.black,),
      ),
    );
  }

  void showDialogBox(BuildContext context) {
    TextEditingController _titlecontroller  = new TextEditingController();
    TextEditingController _desccontroller  = new TextEditingController();

    AlertDialog noteDialog = AlertDialog(
      title: Text("ADD A NOTE"),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextField(
            controller: _titlecontroller,
            decoration: InputDecoration(hintText: "Enter Title"),
          ),
          TextField(
            controller: _desccontroller,
            decoration: InputDecoration(hintText: "Enter Description"),
          )
        ],
      ),
      actions: [
        FlatButton(onPressed: (){
          Provider.of<NotesProvider>(context,listen: false).addNotes(_titlecontroller.text, _desccontroller.text);
          Navigator.of(context).pop();
        },
            child: Text("ADD NOTE")
        )
      ],
    );
showDialog(context: context, builder: (BuildContext context){return noteDialog;});  }
}

class CardList extends StatelessWidget {
late final Notes notes;
late int index;

CardList(this.notes,this.index,);
  @override
  Widget build(BuildContext context) {
    return Slidable(
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,

          ),
          child: ListTile(
            leading: Icon(Icons.note),
            title: Text(notes.title),
            subtitle: Text(notes.description),
            trailing: Icon(Icons.arrow_forward),
          ),
        ),
        secondaryActions: [
          IconSlideAction(
            color: Colors.red,
            caption: 'DELETE',
            icon: Icons.delete,
            onTap: (){
              Provider.of<NotesProvider>(context,listen: false).removeData(index);
            },
          )
        ],
        actionPane: SlidableDrawerActionPane(),
      actionExtentRatio: 0.25,
    );
  }
}
