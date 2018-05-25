(set-env!
 :dependencies
 '[[adzerk/boot-cljs "2.1.4"]
   [org.clojure/clojurescript "1.10.238"]
   [pandeiro/boot-http "0.8.3"]
   [deraen/boot-sass"0.3.1"]
   [reagent "0.8.0"]
   [crisptrutski/boot-cljs-test "0.3.5-SNAPSHOT"]
   [lightscale/recomps "0.0.1-SNAPSHOT"]]
 :source-paths #{"src"}
 :asset-paths #{"assets"})

(require
 '[adzerk.boot-cljs :refer [cljs]]
 '[pandeiro.boot-http :refer [serve]]
 '[deraen.boot-sass :refer [sass]])

(deftask dev []
  (comp
   (watch)
   (speak)
   (cljs)
   (sass)
   (serve :port 8081 :dir "target")
   (target :dir #{"target"})))

(deftask prod []
  (comp
   (speak)
   (cljs :optimizations :advanced)
   (sass :output-style :compressed :source-map false)
   (serve :port 8081 :dir "target")
   (target :dir #{"target"})))
