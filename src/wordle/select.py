words = []
i = 0
with open("src/wordle/words_calc.txt","r") as f:
    words.append(f.readline()[:5])
    while line := f.readline()[:5]:
        is_good = True
        for j,c in enumerate(line):
            if c in words[i]:
                is_good = False
                break
        if is_good:
            words.append(line)
            i += 1

for word in words:
    print(word)
            