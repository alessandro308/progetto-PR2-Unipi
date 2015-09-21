type ide = string;;
type integer = int;;

(*Eccezioni*)
exception WrongMatchException;;
exception EmptyEnvException;;
exception TypeErrorException;;
exception UnboundRecordException;;
exception OutOfBoundException;;

type exp =
	| Ide of ide (*Identificatore*)
	| Ide_t of ide (*Identificatore Tupla*)
	| Ide_f of ide (*Identificatore Funzione*)
	| Int of int (*Valori Interi*)
	| Bool of bool (*Valori Booleani, true=1, false=0*)
	| Add of exp * exp (*Operatori Matematici*)
	| Sub of exp * exp
	| Mul of exp * exp
	| Eq of exp * exp
	| Leq of exp * exp
	| And of exp * exp (*Operatori Logici*)
	| Or of exp * exp
	| Not of exp
	| Function of ide * exp (*Funzione con un parametro, non ricorsiva*)
	| IfThenElse of exp * exp * exp (*Classico If Then Else *)
	| LetIn of ide * exp * exp (*Blocco Let*)
	| FunApply of ide * exp (*Applicazione funzionale Ide(E)*)
	| Tupla of elts (*Espressione Tupla*)
	| GetIndex of exp * exp (*Accesso Elemento Tupla*)
	| GetFirstN of exp * exp (* Seleziona elementi Tupla*)
	| TupleEquals of exp * exp (*Confronto tra tuple*)
	| Map of ide * exp (*Applica funzione ad elementi tupla*)
and
(*Elementi di una tupla*)
	elts = Elemento of exp | Lista of exp list
;;

	(* the empty environment *)
	(* emptyEnv: 'a -> 'b *)
let emptyEnv = fun x -> raise EmptyEnvException;;
let emptyFunEnv = fun x -> failwith "EmptyFunEnvException";;
let emptyTuplaEnv = fun x -> failwith "EmptyTupleEnvException";;
	(*bind: ('a -> 'b) -> ide -> exp -> (ide -> exp ) *)
let bind env (variable: ide) value = fun y ->
			if variable = y then value else env y;;
			
	(*CASTING*)
let asint = function Int x -> x | _ -> failwith "not an integer";;
let asbool = function Bool x -> x | _ -> failwith "not a boolean";;
let exptolista = function Tupla x -> x | _ -> failwith "not a Lista";;
let listatolist = function Lista x -> x | _ -> failwith "not a list";;
let exptolist = function Tupla(x) -> listatolist(x) | _ -> failwith "not a list";;
let typecheck (x:exp) = match x with 
	| Ide _ -> Ide "ide"
	| Ide_t _ -> Ide "ide_t"
	| Ide_f _ -> Ide "ide_f"
	| Int _ -> Ide "Int"
	| Bool _ -> Ide "Bool"
	| Add _ -> Ide "Add"
	| Sub _-> Ide "Sub"
	| Mul _-> Ide "Mul"
	| Eq _-> Ide "Eq"
	| Leq _-> Ide "Leq"
	| And _-> Ide "And"
	| Or _-> Ide "Or"
	| Not _-> Ide "Not"
	| Function _-> Ide "Function"
	| IfThenElse _-> Ide "ITE"
	| LetIn _-> Ide "LetIn"
	| FunApply _-> Ide"FunApply"
	| Tupla _-> Ide "Tupla"
	| GetIndex _-> Ide "GetIndex"
	| GetFirstN _-> Ide "GetFirstN"
	| TupleEquals _-> Ide "TupleEquals"
	| Map _-> Ide "Map"
;; 

	(*SUPPORTO*)
let rec getElement lista index = match lista with
	| [] -> raise OutOfBoundException
	| primo::elems -> if index = 0 then primo else getElement elems (index-1);;
	
let rec first lista number = if number = 0 then [] else
	let lista1 = exptolist(lista) in
		match lista1 with
		| [] -> raise OutOfBoundException
		| primo::elems -> let resto = Tupla(Lista(elems)) in primo :: first resto (number-1) ;;

let rec map lista funct = match lista with
	| [] -> []
	| elem::elems -> (funct elem)::(map elems funct);;


let funDeclr funName (expression: exp) env funenv = match expression with
	| Function  (param, body) -> bind funenv funName (param, body, env)
	| _ -> raise WrongMatchException;;
	
	
let appendE elemExp elemExpLE = let lista = exptolista elemExpLE in
	match lista with
		| Elemento (expr) -> Lista(elemExp::[expr])
		| Lista (exprlist) -> Lista(elemExp::exprlist);;

let rec eval (expression: exp) env funenv tenv =
	match expression with
	| Int i -> Int(i)
	| Ide i -> env i (*UNIRE _UESTI DUE COSI*)
	| Ide_t i -> tenv i
	| Bool i -> Bool(i)
	| Add (e1, e2) -> Int(asint(eval e1 env funenv tenv) + asint(eval e2 env funenv tenv))
	| Sub (e1, e2) -> Int(asint(eval e1 env funenv tenv) - asint(eval e2 env funenv tenv))
	| Mul (e1, e2) -> Int(asint(eval e1 env funenv tenv) * asint(eval e2 env funenv tenv))
	| Eq (e1, e2) -> if (eval e1 env funenv tenv) = (eval e2 env funenv tenv) then Bool(true) else Bool(false)
	| Leq (e1, e2) -> if (eval e1 env funenv tenv) <= (eval e2 env funenv tenv) then Bool(true) else Bool(false)
	| And (e1, e2) -> if asbool(eval e1 env funenv tenv) && asbool(eval e2 env funenv tenv) then Bool(true) else Bool(false)
	| Or (e1, e2) -> if asbool(eval e1 env funenv tenv) || asbool(eval e2 env funenv tenv) then Bool(true) else Bool(false)
	| Not (e1) -> if asbool(eval e1 env funenv tenv) then Bool(false) else Bool(true)
	| IfThenElse (e1, e2, e3) -> if asbool(eval e1 env funenv tenv) then eval e2 env funenv tenv
																else eval e3 env funenv tenv
	| LetIn (id, value, body) -> (match value with 
									| Int value -> let value1 = Int(value) in
														let value = eval value1 env funenv tenv in
															let env1 = bind env id value in
																eval body env1 funenv tenv
									| Tupla lista -> let lista1 = Tupla(lista) in
														let value = eval lista1 env funenv tenv in
															let tenv1 = bind tenv id value in
																eval body env funenv tenv1
									| Function (param, funbody) -> let funzione = Function(param, funbody) in
																		let funenv1 = funDeclr id funzione env funenv in
																			eval body env funenv1 tenv
									| _ -> failwith "unassignable value"
								)
	| GetIndex (id, i) -> let index = asint(eval i env funenv tenv) in
							let lista = exptolist(id) in
								getElement lista index
	| GetFirstN (exp, i) ->	let index = asint(eval i env funenv tenv) in
								let lista = eval exp env funenv tenv in
									Tupla(Lista(first lista index))
	| TupleEquals (exp1, exp2) -> if exptolist(exp1) = exptolist(exp2) then Bool(true) else Bool(false)

	| Map (funx, exp) -> let lista = exptolist(eval exp env funenv tenv) in
								(match lista with
								| [] -> Tupla(Lista([]))
								| x::xs ->let funap = FunApply(funx, x) in
											let val1 = eval funap env funenv tenv in
												let resto = eval (Map(funx, Tupla(Lista(xs)))) env funenv tenv in
													let unita = appendE (val1) (resto)  in
														Tupla(unita)
								)
	| FunApply (funName, arg) -> (*Chiamata di funzione*)
			let value = eval arg env funenv tenv in
					let (param, body, ambiente) = funenv funName in
							let env1 = bind env param value in
								eval body env1 funenv tenv				
	| Tupla(exp) ->  let lista = listatolist(exp) in 
						(match lista with
							| [] -> Tupla (Lista [])
							| x::xs -> let value = eval x env funenv tenv in
										let remaining = Tupla(Lista(xs)) in
											let coda = (eval remaining env funenv tenv) in	
												let risultato = appendE value coda in
													Tupla( risultato )
						)
	| _ -> raise WrongMatchException
;;

(**TEST**)
(*Operatori logici e add*)
(*true, false, true, 6*)
let simpleAnd = And(Bool true, Bool true);;
let doubleAnd = And(Bool true, And( Bool true, Bool false));;
eval simpleAnd emptyEnv emptyFunEnv emptyTuplaEnv;;
eval doubleAnd emptyEnv emptyFunEnv emptyTuplaEnv;;

let letAndEquals = LetIn("x", Int 1, Eq(Ide "x", Int 1));;
eval letAndEquals emptyEnv emptyFunEnv emptyTuplaEnv;;

let simpleAdd = LetIn("x", Int 2, LetIn("y", Int 4, Add(Ide "x", Ide "y")));;
eval simpleAdd emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Valutazione Booleane*)
let confronto = LetIn("x", Int 2, LetIn("y", Int 2, Eq(Ide "x", Ide "y")));;
eval confronto emptyEnv emptyFunEnv emptyTuplaEnv;;

let notConfronto = LetIn("x", Int 2, LetIn("y", Int 2, Not(Eq(Ide "x", Ide "y"))));;
eval notConfronto emptyEnv emptyFunEnv emptyTuplaEnv;;

let ifte = LetIn("x", Int 2, LetIn("y", Int 2, IfThenElse(Not(Eq(Ide "x", Ide "y")), Int 1, Int 2)));;
eval ifte emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Valutazione Tupla*)
(*2::23::4*)
let lista_x = Lista(Ide "x" :: Int(23) :: Add(Ide "x", Int 2) ::[]);;
let exampleList = LetIn("x", Int 2, Tupla(lista_x));;

eval exampleList emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Dichiarazione di tupla*)
let stringa = LetIn("t", Tupla(Lista(Int 0 :: Int 2 :: Int 4 :: [])), Ide_t "t");;
eval stringa emptyEnv emptyFunEnv emptyTuplaEnv;;

(*First 2 elems from tuple*)
(*5::6*)
let tupla = Tupla(Lista(Int (5) :: Int(6) :: Bool(true) :: Int(7) ::[]));;
let primi2 = GetFirstN(tupla, Int(2));;
eval primi2 emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Semplice funzione*)
let funzione = Function("x", Add(Ide "x", Int 2));;
let stringa = LetIn("funx", funzione, FunApply("funx",Int 4));;
eval stringa emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Map(raddoppia, tupla)*)
(*10::12*)
let funzione = Function("x", Mul(Ide "x", Int 2));;
let tupla = Tupla(Lista(Int (5) :: Int(6) :: Bool(true) :: Int(7) ::[]));;

let exampleMap = LetIn("mul2", funzione, Map("mul2", GetFirstN(tupla, Int 2)));;
eval exampleMap emptyEnv emptyFunEnv emptyTuplaEnv;;

(*PDF TEST*)
let funzione = Function("x", Add(Ide "x", Int 5));;
let tupla = Tupla(Lista(Int (5) :: Int(6) :: Bool(true) :: Int(7) ::[]));;
let first2 = GetFirstN(Ide_t "t", Int 2);;
let body = Map("add5", first2);;
let stringa = LetIn("add5", funzione, LetIn("t", tupla, body));;

eval stringa emptyEnv emptyFunEnv emptyTuplaEnv;;

(*Tupla annidate*)
let tupla1 = Tupla(Lista(Int (5) :: Int(6) :: Bool(true) :: Int(7) ::[]));;
let tupla2 = Tupla(Lista(Int (5) :: Int(6) :: Bool(true) :: Int(7) ::[]));;
let tupla0 = Tupla(Lista(tupla1 :: tupla2 :: []));;

eval tupla0 emptyEnv emptyFunEnv emptyTuplaEnv;;
