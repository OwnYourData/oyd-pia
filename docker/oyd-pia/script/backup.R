defaultHeaders <- function(token) {
        c('Accept'        = '*/*', 'Content-Type'  = 'application/json', 'Authorization' = paste('Bearer', token))
}

itemsUrl <- function(url, repo_name) {
        paste0(url, '/api/repos/', repo_name, '/items')
}

getToken <- function(pia_url, app_key, app_secret) {
        auth_url <- paste0(pia_url, '/oauth/token')
        optTimeout <- RCurl::curlOptions(connecttimeout = 10)
        response <- tryCatch(
                RCurl::postForm(auth_url, client_id = app_key, client_secret = app_secret, grant_type = 'client_credentials', .opts = optTimeout),
                error = function(e) { return(NA) })
        if (is.na(response)) {
                return(NA)
        } else {
                if(jsonlite::validate(response[1])){
                        return(rjson::fromJSON(response[1])$access_token)
                } else {
                        return(NA)
                }
        }
}

setupApp <- function(pia_url, app_key, app_secret) {
        app_token <- getToken(pia_url, app_key, app_secret)
        if(is.na(app_token)){
                vector()
        } else {
                c('url' = pia_url, 'app_key' = app_key, 'app_secret' = app_secret, 'token' = app_token)
        }
}

writeItem <- function(app, repo_url, item) {
        headers <- defaultHeaders(app[['token']])
        data <- rjson::toJSON(item)
        response <- tryCatch(
                RCurl::postForm(repo_url, .opts=list(httpheader = headers, postfields = data)),
                error = function(e) { 
                        return(NA) })
        response
}

args = commandArgs(trailingOnly=TRUE)
if(length(args) != 2){
        stop("PIA_URL and APP_SECRET missing as arguments")
}
pia_url <- args[1]
app_key <- 'eu.ownyourdata.service_backup'
app_secret <- args[2]
app <- setupApp(pia_url, app_key, app_secret)
url <- itemsUrl(pia_url, app_key)
samp<-c(0:9,letters,LETTERS,"_")
filename <- paste(sample(samp,20),collapse="")
system('su postgres -c "pg_dump --column-inserts --data-only --table=repo pia > /repos.sql"')
system('su postgres -c "pg_dump --column-inserts --data-only --table=item pia > /items.sql"')
system('rm /archive/*')
system(paste0('zip /archive/', filename, '.zip /items.sql /repos.sql'))
data <- list(timestamp = as.numeric(Sys.time()),
             name = filename,
             link = paste0('https://archive.datentresor.org/',
                           filename, '.zip'),
             '_oydRepoName' = 'Backups')
writeItem(app, url, data)
