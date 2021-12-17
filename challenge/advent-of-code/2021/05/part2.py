import math
import re
import sys


class Grid:
    
    def __init__(self, rows, columns):
        self.rows = rows
        self.columns = columns
        
        self._values = [
            [ 0 for x in range(rows + 1) ]
            for y in range(columns + 1)
        ]
    
    def increment(self, x, y):
        self._values[y][x] += 1
        
    def count_overlap(self, n):
        total = 0
        
        for x in range(self.rows + 1):
            for y in range(self.columns + 1):
                value = self._values[y][x]
                
                if value >= n:
                    total += 1
        
        return total
    
    def __repr__(self):
        return str(self._values)
    
    def __str__(self):
        out = ""
        
        for y in range(self.columns + 1):
            for x in range(self.rows + 1):
                value = self._values[y][x]
                value = ".123456789abcdefghijklmnopqrstuvwxyz"[value]
                
                out += f"{value}"
                
            out += "\n"
        
        return out


lines = []
rows = 0
columns = 0

for line in sys.stdin:
    line = line.strip()

    if not len(line):
        break
    
    matcher = re.search("^(\\d+),(\\d+) -> (\\d+),(\\d+)$", line)
    if not matcher:
        raise ValueError(f"invalid format: {line}")
    
    x1 = int(matcher.group(1))
    y1 = int(matcher.group(2))
    x2 = int(matcher.group(3))
    y2 = int(matcher.group(4))
    
    rows = max(rows, x1, x2)
    columns = max(columns, y1, y2)
    
    lines.append(((x1, y1), (x2, y2)))

grid = Grid(rows, columns)


def sign(x):
    if x >= 0:
        return 1
    else:
        return -1


for start, end in lines:
    x1, y1 = start
    x2, y2 = end
    
    x_sign = sign(x2 - x1)
    y_sign = sign(y2 - y1)
    
    if x1 == x2 or y1 == y2:
        for x in range(x1, x2 + x_sign, x_sign):
            for y in range(y1, y2 + y_sign, y_sign):
                grid.increment(x, y)
    else:
        x = x1
        y = y1
        
        while True:
            grid.increment(x, y)
            
            if x == x2 and y == y2:
                break
            
            x += x_sign
            y += y_sign

print(grid)
print(grid.count_overlap(2))
