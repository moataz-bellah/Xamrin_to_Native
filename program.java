 class program{ 
public   run( ){ 
Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("realmName").build());
Guitar Task = new Guitar();
Task.Make("Gibson");
Task.Model("Les Paul Custom");
Task.Price(649.99);
Task.Owner("N Young");
realm.executeTransaction (transactionRealm -> {
 transactionRealm.insert(Task);});
  RealmResults<Guitar>allGuitars = realm.where(Guitar.class).findAll();
RealmResults<Guitar>lessExpensiveGuitars0 = realm.where(Guitar.class).findAll();
RealmQuery<Guitar>lessExpensiveGuitars = lessExpensiveGuitars0.where().lessThan("Price",400)).;
RealmResults<Guitar>guitarsSortedByMake0 = realm.where(Guitar.class).findAll();
RealmResults<Guitar>guitarsSortedByMake = guitarsSortedByMake0.sort("Make)");
realm.executeTransaction(bla->{
 davidsStrat.Price(1700345.56);
});
  realm.executeTransaction(bla->{
mostExpensiveGuitar.findFirst().deleteFromRealm();
 });
  App app = new App(new AppConfiguration.Builder((myRealmAppId).build());
Credentials anonymousCredentials = Credentials.anonymous();
AtomicReference<User>user = AtomicReference<User>();
app. loginAsync(anonymousCredentials, it -> {
 if (it.isSuccess()) {
user.set(app.currentUser());
 } else {
        Log.e("AUTH", it.getError().toString());
    }
});
awaituseruser.get().logOutAsync( result -> {
    if (result.isSuccess()) {
        Log.v("AUTH", "Successfully logged out.");
    } else {
        Log.e("AUTH", result.getError().toString());
    }
});
  
} 
} 
