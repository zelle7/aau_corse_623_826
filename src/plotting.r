data.path <- "/home/zelle/Playground/selected_topics/eclipse-issues-2001-1009/2002-2003/"
in.file <-"results.out"

d <- read.csv(paste(data.path,in.file,sep="/"), header = F, sep = ",")

dnor <- subset(d, V3=='normal')
dmin <- subset(d, V3=='minor')
dcrit <- subset(d, V3=='critical')
dmajor <- subset(d, V3=='major')

in.file <-"results_date.out"
ddaysSev <- read.csv(paste(data.path,in.file,sep="/"), header = F, sep = ",")
ddays <- subset(ddaysSev, V1=='major')

dmon <- subset(ddays, V2=='1')
dtue <- subset(ddays, V2=='2')
dwed <- subset(ddays, V2=='3')
dthur <- subset(ddays, V2=='4')
dfri <- subset(ddays, V2=='5')
dsat <- subset(ddays, V2=='6')
dsun <- subset(ddays, V2=='7')

png(filename = "/home/zelle/Playground/selected_topics/eclipse-issues-2001-1009/day_of_week_2002.png",
    width = 480, height = 480, units = "px",
    pointsize = 12, bg = "white",
    type = c("Xlib"))


# boxplot(dnor$V6, dmin$V6, dmajor$V6, dcrit$V6, names = c("normal", "minor", "major", "critical"))

boxplot(dmon$V3, dtue$V3, dwed$V3, dthur$V3, dfri$V3, dsat$V3, dsun$V3)

dev.off()
