import matplotlib.pyplot as plt

# The data

mutex_reads = []
mutex_writes = []

rw_reads = []
rw_writes = []

rcu_reads = []
rcu_writes = []

with open("graph_data", "r") as f: 
    # lines = f.readlines()
    nubler_of_readers = 0
    for line in f:
        s0, s1, s2, s3, s4, s5, s6, s7, s8 = line.split()
        #print(s2,s3,s4,s6,s7,s8)
        read = int((int(s2) + int(s3) + int(s4)) / 3)
        write = int((int(s6) + int(s7) + int(s8)) / 3)
        if nubler_of_readers < 20:
            mutex_reads.append(read)
            mutex_writes.append(write)
        elif nubler_of_readers < 40:
            rw_reads.append(read)
            rw_writes.append(write)
        else:
            rcu_reads.append(read)
            rcu_writes.append(write)
        nubler_of_readers += 1

        
print(mutex_reads)
print(rw_reads)
print(rcu_reads)

for i in range(len(rcu_writes)):
    mutex_writes[i] = rcu_writes[i] / mutex_writes[i]
    rw_writes[i] = rcu_writes[i] / rw_writes[i]

x =  ["1", "2", "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
y1 = [2,  15, 27, 35, 40]
y2 = [10, 40, 45, 47, 50]
y3 = [5,  25, 40, 45, 47]

# Initialise the figure and axes.
fig, ax = plt.subplots(1, figsize=(8, 6))

# Set the title for the figure
fig.suptitle('Multiple Lines in Same Plot', fontsize=15)

# Draw all the lines in the same plot, assigning a label for each one to be
# shown in the legend.
ax.plot(x, mutex_reads, color="red", label="mutex -- reads")
ax.plot(x, rw_reads, color="green", label="rw_lock -- reads")
ax.plot(x, rcu_reads, color="blue", label="rcu -- reads")
ax.plot(x, mutex_writes, color="purple", linestyle=(0, (1, 4)), label="rcu / mutex -- writes")
ax.plot(x, rw_writes, color="orange", linestyle=(0, (3, 10)),  label="rcu / rw_lock -- writes")


ax.set_xlabel("number of readers")
ax.set_ylabel("number of reads per second")
# plt.yscale("log")

# Add a legend, and position it on the lower right (with no box)
plt.legend(loc="upper left", frameon=False)

plt.show() 
