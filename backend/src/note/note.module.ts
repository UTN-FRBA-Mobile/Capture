import { Module } from '@nestjs/common';
import { SequelizeModule } from '@nestjs/sequelize';
import { Note } from './note.model';
import { NoteService } from './note.service';
import { NoteController } from './note.controller';

@Module({
  imports: [SequelizeModule.forFeature([Note])],
  providers: [NoteService],
  controllers: [NoteController],
})
export class NoteModule {}
