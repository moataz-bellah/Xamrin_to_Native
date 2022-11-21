 class program{ 
public  func run( )->{ 
let realm = try! Realm()
let Task = Guitar();
Task.Make("Gibson")
Task.Model("Les Paul Custom")
Task.Price(649.99)
Task.Owner("N Young")
try! realm.write {
 realm.add(Task)}
  let allGuitars=realm.objects(Guitar.self)
let lessExpensiveGuitars0=realm.objects(Guitar.self)
let lessExpensiveGuitars = lessExpensiveGuitars0.where { 
)$0.Price<400),}
let guitarsSortedByMake0=realm.objects(Guitar.self)
RealmResults<Guitar>guitarsSortedByMake = guitarsSortedByMake0.sort("Make)");
try! realm.write {
 davidsStrat.Price(1700345.56)
}
  try! realm.write {
 realm.delete(mostExpensiveGuitar)
}
  let app = App(id:(myRealmAppId)
do{
 let user = try await app.login(credentials: Credentials.anonymous)
await openSyncedRealm(user:user)
}
catch {
    print("Error logging in: \(error.localizedDescription)")
}
awaituser.currentUser?.logOut { (error) in
 print("Error")
}
  
} 
} 
