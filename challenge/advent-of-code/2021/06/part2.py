import tqdm


TARGET_DAYS = 256

initial = input("initial> ")

buckets = {
    n: 0 for n in range(8 + 1)
}

for fish in [ int(x) for x in initial.split(",") ]:
    buckets[fish] += 1

print()
print(buckets)

for day in range(1, TARGET_DAYS + 1):
    zero = buckets[0]
    
    for n in range(1, 8 + 1):
        buckets[n - 1] = buckets[n]
    
    buckets[6] += zero
    buckets[8] = zero

    print(day, buckets)

print(sum(buckets.values()))
