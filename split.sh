rm  ./data/users* 2> /dev/null
rm  ./data/importer_done* 2> /dev/null
split -l 10000 ./users.csv data/users_chunk_