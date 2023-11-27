# Stop and remove the current containter.
docker stop localnet
docker rm localnet
docker stop mysql-yx
docker rm mysql-yx

# Build and replace the previous image.
docker build --force-rm -t "yixing/localnet" .

# Clean unknown images
docker image prune