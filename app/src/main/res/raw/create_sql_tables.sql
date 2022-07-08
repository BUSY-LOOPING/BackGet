CREATE TABLE uri_model_table (`primary_key` TEXT PRIMARY KEY NOT NULL,
                               `regular` TEXT NOT NULL,
                               `full` TEXT NOT NULL,
                               `raw` TEXT NOT NULL,
                               FOREIGN KEY (`primary_key`) REFERENCES liked_table(`image_id`) ON DELETE CASCADE);;;

CREATE TABLE user_model_table (`user_id` TEXT PRIMARY KEY NOT NULL,
                               `name` TEXT NOT NULL,
                               `bio` TEXT,
                               `username` TEXT NOT NULL,
                               `first_name` TEXT,
                               `last_name` TEXT,
                               `email` TEXT,
                               `downloads` INTEGER,
                               `instagram_username` TEXT,
                               `twitter_username` TEXT,
                               `portfolio_url` TEXT,
                               `for_hire` BOOLEAN DEFAULT FALSE,
                               `is_currentUser` BOOLEAN DEFAULT FALSE);;;

CREATE TABLE liked_table (`image_id` TEXT PRIMARY KEY,
                           `description` TEXT,
                           `user_model` TEXT NOT NULL,
                           `likes` INTEGER NOT NULL,
                           `views` TEXT,
                           `liked` BOOLEAN NOT NULL,
                           FOREIGN KEY (`user_model`) REFERENCES user_model_table (`user_id`) ON DELETE CASCADE);;;

CREATE TABLE user_profile_image_table (`id` TEXT PRIMARY KEY NOT NULL,
                                        `small` TEXT NOT NULL,
                                        `medium` TEXT NOT NULL,
                                        `large` TEXT NOT NULL,
                                        FOREIGN KEY (`id`) REFERENCES user_model_table(`user_id`) ON DELETE CASCADE) ;;;


CREATE TABLE download_table(`id` TEXT NOT NULL,
                             `path` TEXT NOT NULL,
                             `date` DATE NOT NULL,
                             `download_res` INTEGER NOT NULL,
                             FOREIGN KEY (`id`) REFERENCES liked_table(`image_id`),
                             PRIMARY KEY(`id`, `path`));;;


