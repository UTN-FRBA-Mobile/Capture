import { Module } from '@nestjs/common';
import { SequelizeModule } from '@nestjs/sequelize';
import { UsersModule } from './users/user.module';
import { User } from './users/user.model';
import { NoteModule } from './note/note.module';
import { AuthModule } from './auth/auth.module';

@Module({
  imports: [
    SequelizeModule.forRoot({
      dialect: 'sqlite',
      storage: '.db/data.sqlite3',
      autoLoadModels: true,
      synchronize: true,
      models: [User],
    }),
    UsersModule,
    NoteModule,
    AuthModule,
  ],
  controllers: [],
  providers: [],
})
export class AppModule {}
