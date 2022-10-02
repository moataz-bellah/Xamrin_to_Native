 class myClass4{ 
public   test2( ){ 
FirebaseFirestore fd = FirebaseFirestore.getInstance();
fd.collection("users").document("alovelace");
fd.collection("users").get();
CollectionReference usersRef = fd.collection("users");
usersRef.whereGreaterThanOrEqualTo("State","CA");

} 
public   run2( String coll,String data){ 
FirebaseFirestore fd2 = FirebaseFirestore.getInstance();
fd2.collection(coll).document(data);
fd2.collection(coll).get();
CollectionReference usersRef2 = fd2.collection("users");
usersRef2.whereGreaterThanOrEqualTo("Name",data);

} 
public   read2( String coll,String document){ 
FirebaseFirestore db = FirebaseFirestore.getInstance();
CollectionReference refd2 = db.collection(coll);
DocumentReference doc = refd2.document(document);

} 
public   add2( ){ 
Map<String,String>map = new Map<>();
map["name"] = "Mike";map["age"] = 22;map["salary"] = 2000;map["car"] = "BMW";FirebaseFirestore db2 = FirebaseFirestore.getInstance();
db2.collection("users").;

} 
public   update2( String collec){ 
Map<String,String>map = new Map<>();
map["name"] = "Ahmed";map["age"] = 24;map["salary"] = 2500;map["car"] = "Opel";FirebaseFirestore db3 = FirebaseFirestore.getInstance();
.;

} 
} 
