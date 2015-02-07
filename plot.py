import matplotlib.pyplot as plt
import numpy as np
CAP=400000.0
days=['Mon','Tue','Wed','Thu','Fri','Sat','Sun']

def twodec(num):
    if num.find(".") > 0:
        return float(num[0:num.find(".")+3])
    else:
        return float(num)

labels= []
phs = []
with open('/tmp/errorStatsByDowAndHour.tsv','r') as f:
# with open('/shared/errorStatsByDowAndHour.tsv','r') as f:
    for line in f:
        print line
        toks = ([min(twodec(l.strip()),CAP)
                    if l.find("-") < 0
                    else l.strip()
                    for l in line.split('\t')])
        labels.append(toks[0])
        del toks[0]
        thresh = toks[1]+toks[3]*2.5
        toks.append(thresh)
        phs.append(toks)

for p in phs:
    print p

matarr = np.array([np.array(list(_)) for _ in phs]).T

NTICKS=  42
plabels = [labels[x] if x % (len(labels)/NTICKS) == 3 else ""  for x in range(len(labels))]

xax = np.arange(len(labels))
plt.xticks(xax,tuple(plabels),rotation=60,fontsize=10)
plt.plot(xax,matarr.T)
plt.legend(['Min','Mean','Max','Stdev','Thresh'],loc='upper left')
plt.title('PAF DQM Failed Records by Day/Hour of Week')
plt.xlabel('Day/Hour of Week')
plt.ylabel('Errored Recs')
plt.grid()
# plt.show()
plt.savefig("/tmp/erroredRecs.png") ;
