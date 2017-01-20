http://www.bl.uk/bibliographic/datafree.html#lod
http://www.bl.uk/bibliographic/download.html


Query to http://bnb.data.bl.uk/flint-sparql to list journals


PREFIX dct: <http://purl.org/dc/terms/> 
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT * WHERE {
   ?o a <http://purl.org/ontology/bibo/Periodical> .
   ?o dct:title ?title .
}
LIMIT 10




Lets find out about TriangleJournals as a publisher



PREFIX dct: <http://purl.org/dc/terms/> 
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT * WHERE {
   <http://bnb.data.bl.uk/id/agent/TriangleJournals> ?p ?o .
}
LIMIT 10
