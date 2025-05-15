PROJECT_NAME=$(grep 'name := ' build.sbt | awk -F'"' '{print $2}')
if ! lsof -i :$PORT > /dev/null; then
    echo "Starting"
    ./dist/bin/$PROJECT_NAME -jvm-debug $DEBUG_PORT -Dhttp.port=$PORT > webhook.log 2>&1 &
fi