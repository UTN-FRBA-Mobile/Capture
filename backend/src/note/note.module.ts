import { Module } from '@nestjs/common';
import { SequelizeModule } from '@nestjs/sequelize';
import { Note } from './note.model';
import { Tag } from '../tag/tag.model';
import { NoteService } from './note.service';
import { NoteController } from './note.controller';
import { TagModule } from '../tag/tag.module';

@Module({
  imports: [SequelizeModule.forFeature([Note, Tag]), TagModule],
  providers: [NoteService],
  controllers: [NoteController],
})
export class NoteModule {}
