VERSION=$(grep 'version := ' build.sbt | awk -F'"' '{print $2}')
PROJECT_NAME=$(grep 'name := ' build.sbt | awk -F'"' '{print $2}')
rm -rf dist
sbt clean compile dist
unzip target/universal/$PROJECT_NAME-$VERSION.zip
mv $PROJECT_NAME-$VERSION dist

# sbt clean compile stage