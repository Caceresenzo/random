import sys

GRID_SIZE = 5


class Grid:
    
    def __init__(self, lines):
        self._values = [
            [ int(v) for v in line.replace("  ", " ").split(" ") ]
            for line in lines
        ]
        
        self._founds = [
            [ False for _ in range(GRID_SIZE)]
            for _ in range(GRID_SIZE)
        ]
    
    def mark(self, number):
        for x in range(GRID_SIZE):
            for y in range(GRID_SIZE):
                if self._values[y][x] == number:
                    self._founds[y][x] = True
    
    def sum_unmarkeds(self):
        total = 0
        
        for x in range(GRID_SIZE):
            for y in range(GRID_SIZE):
                if not self._founds[y][x]:
                    total += self._values[y][x]
        
        return total
    
    def has_winning_row(self):
        for y in range(GRID_SIZE):
            for x in range(GRID_SIZE):
                if not self._founds[y][x]:
                    break
            else:
                return True
        
        return False
    
    def has_winning_column(self):
        for x in range(GRID_SIZE):
            for y in range(GRID_SIZE):
                if not self._founds[y][x]:
                    break
            else:
                return True
        
        return False
    
    def is_winning(self):
        return self.has_winning_row() or self.has_winning_column()
    
    def __repr__(self):
        return str(self._values)
    
    def __str__(self):
        out = ""
        
        for x in range(GRID_SIZE):
            for y in range(GRID_SIZE):
                value = self._values[y][x]
                found = self._founds[y][x]
                
                bound_left = "[" if found else " "
                bound_right = "]" if found else " "
                
                out += f"{bound_left}{value:3}{bound_right}"
                
            out += "\n"
        
        return out


skip_next = False
numbers = None
grids = []

grid_lines = []

for line in sys.stdin:
    line = line.strip()

    if skip_next:
        skip_next = False
        continue
    
    if numbers is None:
        numbers = list(map(int, line.split(",")))
        skip_next = True
        continue

    grid_lines.append(line)
    if len(grid_lines) == GRID_SIZE:
        grids.append(Grid(grid_lines))
        grid_lines = []
        skip_next = True

for number in numbers:
    for grid in grids:
        grid.mark(number)
        
        if grid.is_winning():
            others = grid.sum_unmarkeds()
            
            print(others * number)
            exit(0)
