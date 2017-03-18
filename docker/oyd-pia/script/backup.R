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

# convert response string into data.frame
r2d <- function(response){
        if (is.na(response)) {
                data.frame()
        } else {
                if (nchar(response) > 0) {
                        retVal <- rjson::fromJSON(response)
                        if(length(retVal) == 0) {
                                data.frame()
                        } else {
                                if ('error' %in% names(retVal)) {
                                        data.frame()
                                } else {
                                        if (!is.null(retVal$message)) {
                                                if (retVal$message == 
                                                    'error.accessDenied') {
                                                        data.frame()
                                                } else {
                                                        # convert list to data.frame
                                                        jsonlite::fromJSON(response)
                                                }
                                        } else {
                                                jsonlite::fromJSON(response)
                                        }
                                }
                        }
                } else {
                        data.frame()
                }
        }
}

# read data from PIA
readItems <- function(app, repo_url) {
        if (length(app) == 0) {
                data.frame()
                return()
        }
        headers <- defaultHeaders(app[['token']])
        url_data <- paste0(repo_url, '?size=2000')
        header <- RCurl::basicHeaderGatherer()
        doc <- tryCatch(
                RCurl::getURI(url_data,
                              .opts=list(httpheader = headers),
                              headerfunction = header$update),
                error = function(e) { return(NA) })
        response <- NA
        respData <- data.frame()
        if(!is.na(doc)){
                recs <- tryCatch(
                        as.integer(header$value()[['X-Total-Count']]),
                        error = function(e) { return(0)})
                if(recs > 2000) {
                        for(page in 0:floor(recs/2000)){
                                url_data <- paste0(repo_url,
                                                   '?page=', page,
                                                   '&size=2000')
                                response <- tryCatch(
                                        RCurl::getURL(url_data,
                                                      .opts=list(httpheader=headers)),
                                        error = function(e) { return(NA) })
                                subData <- r2d(response)
                                if(nrow(respData)>0){
                                        respData <- rbind(respData, subData)
                                } else {
                                        respData <- subData
                                }
                        }
                } else {
                        response <- tryCatch(
                                RCurl::getURL(url_data,
                                              .opts=list(httpheader = headers)),
                                error = function(e) { return(NA) })
                        respData <- r2d(response)
                }
        }
        respData
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

deleteRepo <- function(app, repo_url){
        allItems <- readItems(app, repo_url)
        lapply(allItems$id, 
               function(x) deleteItem(app, repo_url, x))
}

args = commandArgs(trailingOnly=TRUE)
if(length(args) != 2){
        stop("PIA_URL and APP_SECRET missing as arguments")
}
pia_url <- args[1]
app_key <- 'eu.ownyourdata.service_backup'
app_secret <- args[2]
app <- setupApp(pia_url, app_key, app_secret)
url <- itemsUrl(pia_url, 'eu.ownyourdata.backup.status')
bs <- readItems(app, url)
if(nrow(bs) > 1){
        deleteRepo(app, url)
        bs <- data.frame()
}
backup_active <- TRUE
if(nrow(bs) == 1){
        backup_active <- bs[1,'active']
} else {
        data <- list(
                active = TRUE,
                '_oydRepoName' = 'Backup Status'
        )
        writeItem(app, url, data)
}

if(backup_active){
        url <- itemsUrl(pia_url, 'eu.ownyourdata.backup')
        samp<-c(0:9,letters,LETTERS,"_")
        filename <- paste(sample(samp,20),collapse="")
        system('su postgres -c "pg_dump --column-inserts --data-only --table=store pia > /stores.sql"')
        system('su postgres -c "pg_dump --column-inserts --data-only --table=repo pia > /repos.sql"')
        system('su postgres -c "pg_dump --column-inserts --data-only --table=item pia > /items.sql"')
        system('rm /archive/*')
        system(paste0('zip /archive/', filename, '.zip /stores.sql /items.sql /repos.sql'))
        data <- list(timestamp = as.numeric(Sys.time()),
                     name = filename,
                     link = paste0('https://archive.datentresor.org/',
                                   filename, '.zip'),
                     '_oydRepoName' = 'Backups')
        writeItem(app, url, data)
}