 class myClass : Object{ 
@Persisted(primaryKey: true) var Id: ObjectId
func Id(x:ObjectId) -> Void{
 self.Id=x}
func Id()->ObjectId{
 return self.Id}
@Persisted var Make: string
func Make(x:string) -> Void{
 self.Make=x}
func Make()->string{
 return self.Make}
@Persisted var Model: string
func Model(x:string) -> Void{
 self.Model=x}
func Model()->string{
 return self.Model}
@Persisted var Price: double
func Price(x:double) -> Void{
 self.Price=x}
func Price()->double{
 return self.Price}
@Persisted var Owner: string
func Owner(x:string) -> Void{
 self.Owner=x}
func Owner()->string{
 return self.Owner}
} 
