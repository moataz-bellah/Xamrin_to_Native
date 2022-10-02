 class myClass5{ 
public  func test2( )->{ 
var fd :Firestore = Firestore.firestore();
fd.collection("users").document("alovelace");
fd.collection("users").getDocuments();
var usersRef :CollectionReference = fd.collection("users");
usersRef.whereField("State", isGreaterThanOrEqualTo: "CA");

} 
public  func run2( var coll :String,var data :String)->{ 
var fd2 :Firestore = Firestore.firestore();
fd2.collection(coll).document(data);
fd2.collection(coll).getDocuments();
var usersRef2 :CollectionReference = fd2.collection("users");
usersRef2.whereField("Name", isGreaterThanOrEqualTo: data);

} 
public  func read2( var coll :String,var document :String)->{ 
var db :Firestore = Firestore.firestore();
var refd2 :CollectionReference = db.collection(coll);
var doc :DocumentReference = refd2.document(document);

} 
public  func add2( )->{ 
var map = [String: String]()
map["name"] = "Mike";map["age"] = 22;map["salary"] = 2000;map["car"] = "BMW";var db2 :Firestore = Firestore.firestore();
db2.collection("users").;

} 
public  func update2( var collec :String)->{ 
var map = [String: String]()
map["name"] = "Ahmed";map["age"] = 24;map["salary"] = 2500;map["car"] = "Opel";var db3 :Firestore = Firestore.firestore();
.;

} 
} 
