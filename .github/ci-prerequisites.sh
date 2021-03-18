# Reclaim disk space, otherwise we only have 13 GB free at the start of a job
echo "Reclaim disk space."
df -h
time docker rmi node:10 node:12 mcr.microsoft.com/azure-pipelines/node8-typescript:latest
# That is 18 GB
time sudo rm -rf /usr/share/dotnet
# That is 1.2 GB
time sudo rm -rf /usr/share/swift
echo "Reclaim disk space end."
df -h