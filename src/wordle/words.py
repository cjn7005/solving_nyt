import pandas as pd

letters = {
    1:[],
    2:[],
    3:[],
    4:[],
    5:[]
}

with open('wordle/wordle.txt') as f:
    f.readline()
    for line in f.readlines():
        for i in range(5):
            letters[i+1].append(line[i])

df = pd.DataFrame(letters)
df.to_csv('wordle/words.csv')

counts = df.apply(pd.Series.value_counts).fillna(0).astype(int)
counts_by_idx = [[],[],[],[],[]]
for i in range(5):
    t = counts[i+1].sort_values(ascending=False).head()
    counts_by_idx[i] = t.index

scores_by_word = pd.DataFrame(columns=['word','score'])
for a in range(len(counts_by_idx[0])):
    for b in range(len(counts_by_idx[1])):
        for c in range(len(counts_by_idx[2])):
            for d in range(len(counts_by_idx[3])):
                for e in range(len(counts_by_idx[4])):
                    word = f'{counts_by_idx[0][a]}{counts_by_idx[1][b]}{counts_by_idx[2][c]}{counts_by_idx[3][d]}{counts_by_idx[4][e]}'
                    score = counts.loc[counts_by_idx[0][a],1]+counts.loc[counts_by_idx[1][b],2]+counts.loc[counts_by_idx[2][c],3]+counts.loc[counts_by_idx[3][d],4]+counts.loc[counts_by_idx[4][e],5]
                    scores_by_word = pd.concat([scores_by_word,pd.DataFrame({'word':[word],'score':[score]})])

scores_by_word.to_csv('wordle/scores.csv')

scores_by_word = scores_by_word.sort_values(by='score',ascending=False)
mask = scores_by_word.apply(lambda x: pd.Series([x['word'][i] for i in range(5)]).is_unique,axis = 1)

scores_by_word = scores_by_word[mask]

def foo(x: pd.Series) -> bool:
    with open('wordle/wordle.txt') as f:
        f.readline()
        for line in f.readlines():
            if (line[:5] == x['word']):
                return True
    return False

mask = scores_by_word.apply(foo,axis=1)

scores_by_word = scores_by_word[mask]

with open('wordle/words_calc.txt','w') as f:
    for i in range(len(scores_by_word)):
        f.write(f'{scores_by_word.iloc[i]['word']} {scores_by_word.iloc[i]['score']}\n')
