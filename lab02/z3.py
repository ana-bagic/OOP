def mymax1(iterable, key):
    max_x = max_key = None
    for x in iterable:
        if max_x is None or key(x) > max_key:
            max_key = key(x)
            max_x = x
    return max_x
  
def mymax2(iterable):
    key = lambda x: x
    max_x = max_key = None
    for x in iterable:
        if max_x is None or key(x) > max_key:
            max_key = key(x)
            max_x = x
    return max_x

arr_int = [1, 3, 5, 7, 4, 6, 9, 2, 0]
arr_char = "Suncana strana ulice"
arr_str = ["Gle", "malu", "vocku", "poslije", "kise",
    "Puna", "je", "kapi", "pa", "ih", "njise"]
D = {'burek':8, 'buhtla':5, 'kruh':6, 'kroasan':10}
people = [("James", "Potter"), ("Sirius", "Black"),
    ("Remus", "Lupin"), ("Peter", "Pettigrew")]
  
print(mymax1(arr_str, lambda x: len(x))) #po duljini
print(mymax2(arr_str)) #prirodan order
print(mymax2(arr_char)) #prirodan order
print(mymax2(arr_int)) #prirodan order
print(mymax1(D, D.get)) #po cijeni
print(mymax2(people)) #po imenu
print(mymax1(people, lambda x: x[1])) #po prezimenu