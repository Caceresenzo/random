TARGET_DAYS = 80

initial = input("initial> ")

fishs = [ int(x) for x in initial.split(",") ]


def print_state(day, fishs):
    message = f"After {day:>2} day{'s' if day > 1 else ' '}"
    states = ",".join([ str(x) for x in fishs ])
    
    if day == 0:
        message = "Initial state"
    
    print(f"{message}: {states}")


print()
print_state(0, fishs)

for day in range(1, TARGET_DAYS + 1):
    for index in range(len(fishs)):
        fishs[index] -= 1
        
        if fishs[index] == -1:
            fishs[index] = 6
            
            fishs.append(8)

    print_state(day, fishs)

print(len(fishs))